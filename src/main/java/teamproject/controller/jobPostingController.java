package teamproject.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;
import java.io.PrintWriter;

import teamproject.util.DBConn;
import teamproject.vo.JobpostingVO;
import teamproject.vo.ResumeVO;
import teamproject.vo.UserVO;

public class jobPostingController 
{
	public jobPostingController(HttpServletRequest request, HttpServletResponse response, String[] comments) throws ServletException, IOException 
	{
		// 채용공고 리스트
		if(comments[comments.length-1].equals("jobList.do"))
		{
				jobList(request, response);
		// 채용공고 상세
		}else if(comments[comments.length-1].equals("jobView.do"))
		{
				jobView(request, response);
		// 채용공고 지원	
		}else if(comments[comments.length-1].equals("jobApply.do")) 
		{
			if(request.getMethod().equals("GET"))
			{
				jobApply(request, response);
			}else if(request.getMethod().equals("POST"))
			{
				jobApplyOk(request, response);
			}
		}else if(comments[comments.length-1].equals("getresume.do")) 
		{
			if(request.getMethod().equals("GET"))
			{
				getResume(request, response);
			}else if(request.getMethod().equals("POST"))
			{
			//	jobApplyOk(request, response);
			}
		}
	}
	
	// 채용공고 리스트
	public void jobList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		List<JobpostingVO> jList = new ArrayList<JobpostingVO>();
		List<JobpostingVO> jlList = new ArrayList<JobpostingVO>();
		
		HttpSession session = request.getSession();
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
//			경로 확인 필요
			response.sendRedirect(request.getContextPath()+"/user/login_p.do");
		} else {
			
			String searchValue = request.getParameter("index_search");
			if(searchValue == null || searchValue.equals("")) {
				searchValue = "";
			}
			System.out.println("searchValue : " +  searchValue);
			
			Connection conn = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;
			
			PreparedStatement psmtL = null;
			ResultSet rsL = null;
			
			try {
				conn = DBConn.conn();
				
				String sql = "SELECT c.company_no"
						   + " , company_name"
						   + " , company_logo"
						   + " , j.job_posting_no"
						   + " , job_posting_title"
						   + " , COUNT(jl.job_posting_like_no) AS like_count"
						   + " FROM company c, job_posting j, job_posting_like jl"
						   + " WHERE c.company_no = j.company_no"
						   + " AND jl.job_posting_no = j.job_posting_no"
						   + " AND job_posting_state = 'E'"
						   + " AND job_posting_like_state = 'E'"
						   + " AND company_name like CONCAT('%', ?, '%')"
						   + " GROUP BY c.company_no, company_name, company_logo, j.job_posting_no, job_posting_title"
						   + " ORDER BY like_count desc"
						   + " LIMIT 0, 9";
				
				psmt = conn.prepareStatement(sql);
				psmt.setString(1, searchValue);
				rs = psmt.executeQuery();
				
				while(rs.next())
				{
					JobpostingVO jpvo = new JobpostingVO();
					
					jpvo.setCompany_no(rs.getInt("company_no"));
					jpvo.setCompany_logo(rs.getString("company_logo"));
					jpvo.setCompany_name(rs.getString("company_name"));
					jpvo.setJob_posting_no(rs.getInt("job_posting_no"));
					jpvo.setJob_posting_title(rs.getString("job_posting_title"));
					jpvo.setLike_count(rs.getString("like_count"));
					
					jList.add(jpvo);
				}
				request.setAttribute("jList", jList);
				
				String sqlL = "SELECT c.company_no"
						   	+ " , company_name"
						   	+ " , company_logo"
						   	+ " , j.job_posting_no"
						   	+ " , job_posting_title"
						   	+ " FROM company c, job_posting j"
						   	+ " WHERE c.company_no = j.company_no"
						   	+ " AND job_posting_state = 'E'"
						   	+ " ORDER BY job_posting_registration_date desc"
						   	+ " LIMIT 0, 8";
				
				psmtL = conn.prepareStatement(sqlL);				
				rsL = psmtL.executeQuery();
				
				while(rsL.next())
				{
					JobpostingVO jpLvo = new JobpostingVO();
					
					jpLvo.setCompany_no(rsL.getInt("company_no"));
					jpLvo.setCompany_logo(rsL.getString("company_logo"));
					jpLvo.setCompany_name(rsL.getString("company_name"));
					jpLvo.setJob_posting_no(rsL.getInt("job_posting_no"));
					jpLvo.setJob_posting_title(rsL.getString("job_posting_title"));
					
					jlList.add(jpLvo);
				}
				request.setAttribute("jlList", jlList);
				
				request.getRequestDispatcher("/WEB-INF/jobPosting/jobList.jsp").forward(request, response);
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try {
					DBConn.close(rs, psmt, conn);
					DBConn.close(rsL, psmtL, null);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void jobView(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		List<JobpostingVO> jpList = new ArrayList<JobpostingVO>();
		
		HttpSession session = request.getSession();
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
//			경로 확인 필요
			response.sendRedirect(request.getContextPath()+"/user/login_p.do");
		} else {
						
			int jobPostingNo = Integer.parseInt(request.getParameter("job_posting_no"));
			
			Connection conn = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;
			
			try
			{
				conn = DBConn.conn();
				
				String sql = " SELECT c.company_no"
						   + " , company_name"
						   + " , company_location"
						   + " , company_employee"
						   + " , company_industry"
						   + " , company_anniversary"
						   + " , job_posting_no"
						   + " , job_posting_period"
						   + " , job_posting_title"
						   + " , job_posting_content"
						   + " FROM company c, job_posting j"
						   + " WHERE c.company_no = j.company_no"
						   + " AND job_posting_state = 'E'"
						   + " AND j.job_posting_no = ?";
				
				psmt = conn.prepareStatement(sql);				
				psmt.setInt(1, jobPostingNo);
				rs = psmt.executeQuery();
				
				if(rs.next()) {
					JobpostingVO jpvo = new JobpostingVO();
					
					jpvo.setCompany_no(rs.getInt("company_no"));
					jpvo.setCompany_name(rs.getString("company_name"));
					jpvo.setCompany_location(rs.getString("company_location"));
					jpvo.setCompany_employee(rs.getString("company_employee"));
					jpvo.setCompany_industry(rs.getString("company_industry"));
					jpvo.setCompany_anniversary(rs.getString("company_anniversary"));
					jpvo.setJob_posting_no(rs.getInt("job_posting_no"));
					jpvo.setJob_posting_period(rs.getString("job_posting_period"));
					jpvo.setJob_posting_title(rs.getString("job_posting_title"));
					jpvo.setJob_posting_content(rs.getString("job_posting_content"));
					
					jpList.add(jpvo);		
				}
				request.setAttribute("jpList", jpList);
				
				request.getRequestDispatcher("/WEB-INF/jobPosting/jobView.jsp").forward(request, response);
			} catch(Exception e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					DBConn.close(rs, psmt, conn);
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void jobApply(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		List<JobpostingVO> jpList = new ArrayList<JobpostingVO>();
		List<ResumeVO> rList = new ArrayList<ResumeVO>();
		
		HttpSession session = request.getSession();
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
//			경로 확인 필요
			response.sendRedirect(request.getContextPath()+"/user/login_p.do");
		} else {
			
//			int resumeNo = Integer.parseInt(request.getParameter("resume_no"));
			
			int userNo = loginUser.getUser_no();
			int jobPostingNo = Integer.parseInt(request.getParameter("job_posting_no"));
			
			Connection conn = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;
			
			PreparedStatement psmtR = null;
			ResultSet rsR = null;
			
			PreparedStatement psmtRd = null;
			ResultSet rsRd = null;
			
			try
			{
				conn = DBConn.conn();
				
				String sql = " SELECT c.company_no"
						   + " , company_name"
						   + " , company_location"
						   + " , company_employee"
						   + " , company_industry"
						   + " , company_anniversary"
						   + " , j.job_posting_no"
						   + " , job_posting_period"
						   + " , job_posting_title"
						   + " , job_posting_content"
						   + " FROM company c, job_posting j"
						   + " WHERE c.company_no = j.company_no"
						   + " AND job_posting_state = 'E'"
						   + " AND j.job_posting_no = ?";
				
				psmt = conn.prepareStatement(sql);
				psmt.setInt(1, jobPostingNo);
				rs = psmt.executeQuery();
				
				if(rs.next()) {
					JobpostingVO jpvo = new JobpostingVO();
					
					jpvo.setCompany_no(rs.getInt("company_no"));
					jpvo.setCompany_name(rs.getString("company_name"));
					jpvo.setCompany_location(rs.getString("company_location"));
					jpvo.setCompany_employee(rs.getString("company_employee"));
					jpvo.setCompany_industry(rs.getString("company_industry"));
					jpvo.setCompany_anniversary(rs.getString("company_anniversary"));
					jpvo.setJob_posting_no(rs.getInt("job_posting_no"));
					jpvo.setJob_posting_period(rs.getString("job_posting_period"));
					jpvo.setJob_posting_title(rs.getString("job_posting_title"));
					jpvo.setJob_posting_content(rs.getString("job_posting_content"));
					
//					jpList.add(jpvo);		
					request.setAttribute("jpvo", jpvo);
				}
				
				
				String sqlR = " SELECT resume_no"
							+ " , resume_title"
							+ " , resume_tenure_start"
							+ " , resume_tenure_end"
							+ " , resume_area"
							+ " , resume_job"
							+ " , resume_salary"
							+ " FROM resume"
							+ " WHERE user_no = ?";
				
				psmtR = conn.prepareStatement(sqlR);
				psmtR.setInt(1, userNo);
				rsR = psmtR.executeQuery();
				
				while(rsR.next()) {
					ResumeVO rvo = new ResumeVO();
					
					rvo.setResume_no(rsR.getInt("resume_no"));
					rvo.setResume_title(rsR.getString("resume_title"));
					rvo.setResume_tenure_start(rsR.getString("resume_tenure_start"));
					rvo.setResume_tenure_end(rsR.getString("resume_tenure_end"));
					rvo.setResume_area(rsR.getString("resume_area"));
					rvo.setResume_job(rsR.getString("resume_job"));
					rvo.setResume_salary(rsR.getString("resume_salary"));
					
					rList.add(rvo);		
				}
				request.setAttribute("rList", rList);
				
				request.getRequestDispatcher("/WEB-INF/jobPosting/jobApply.jsp").forward(request, response);
			} catch(Exception e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					DBConn.close(rs, psmt, conn);
					DBConn.close(rsR, psmtR, null);
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void getResume(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		ResumeVO vo = null;
		
		HttpSession session = request.getSession();
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
//			경로 확인 필요
			response.sendRedirect(request.getContextPath()+"/user/login_p.do");
		} else {
			
			int resumeNo = Integer.parseInt(request.getParameter("resume_no"));
			
			Connection conn = null;
			PreparedStatement psmt = null;
			ResultSet rs = null;
			
			try
			{
				conn = DBConn.conn();
				
				String sqlR = " SELECT resume_no"
						+ " , resume_title"
						+ " , resume_tenure_start"
						+ " , resume_tenure_end"
						+ " , resume_area"
						+ " , resume_job"
						+ " , resume_salary"
						+ " FROM resume"
						+ " WHERE resume_no = ?";
				
				psmt = conn.prepareStatement(sqlR);
				psmt.setInt(1, resumeNo);
				rs = psmt.executeQuery();
				
				if(rs.next()) {
//					ResumeVO rvo = new ResumeVO();
					JSONObject json = new JSONObject();
					
					//  json 객체에 데이터 넣기
					json.put("resume_no", rs.getInt("resume_no"));
					json.put("resume_title", rs.getString("resume_title"));
					json.put("resume_tenure_start", rs.getString("resume_tenure_start"));
					json.put("resume_tenure_end", rs.getString("resume_tenure_end"));
					json.put("resume_area", rs.getString("resume_area"));
					json.put("resume_job", rs.getString("resume_job"));
					json.put("resume_salary", rs.getString("resume_salary"));
				
					// 응답의 Content-Type을 JSON으로 설정
					response.setContentType("application/json");
					response.setCharacterEncoding("UTF-8");
				}
			} catch(Exception e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					DBConn.close(rs, psmt, conn);
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	public void jobApplyOk(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		HttpSession session = request.getSession();
		UserVO loginUser = (UserVO) session.getAttribute("loginUser");
		
		if(loginUser == null) {
//			경로 확인 필요
			response.sendRedirect(request.getContextPath()+"/user/login_p.do");
		} else {
			
			int jobPostingNo = Integer.parseInt(request.getParameter("job_posting_no"));
			int companyNo = Integer.parseInt(request.getParameter("company_no"));
			int resumeNo = Integer.parseInt(request.getParameter("resume_no"));
			int userNo = loginUser.getUser_no();
			
			Connection conn = null;
			PreparedStatement psmt = null;
			
			try
			{
				conn = DBConn.conn();
				
//				지원자 관리에 해당 내용이 들어가게끔 처리 필요
				String sql = " INSERT INTO applicant ("
						   + "  job_posting_no"
						   + " , company_no"
						   + " , resume_no"
						   + " , user_no"
						   + " ) VALUES ("
						   + "  ?"
						   + " , ?"
						   + " , ?"
						   + " , ?"
						   + " )";
				
				psmt = conn.prepareStatement(sql);
				psmt.setInt(1, jobPostingNo);
				psmt.setInt(2, companyNo);
				psmt.setInt(3, resumeNo);
				psmt.setInt(4, userNo);
				
				psmt.executeUpdate();
				
				response.sendRedirect(request.getContextPath()+"/jobPosting/jobView.do?job_posting_no="+ jobPostingNo);
			} catch(Exception e)
			{
				e.printStackTrace();
			} finally
			{
				try
				{
					DBConn.close(psmt, conn);
				} catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
