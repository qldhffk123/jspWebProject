package com.dailyT.action.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dailyT.action.Action;
import com.dailyT.repository.AdminRepository;
import com.dailyT.util.Script;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

public class ProregProcAction implements Action{
	private static final String TAG="SubregProcAction : ";
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//1. 업로드할 파일의 경로를 지정하고, 받아와야할 request 값을 cos 라이브러리로 불러옴
		String realPath=request.getServletContext().getRealPath("upload");
		
		try {
			MultipartRequest multi=new MultipartRequest(request,
														realPath,
														1024*1024*5,
														"UTF-8",
														new DefaultFileRenamePolicy()
														);
			
		//2. 변수에 받아온 값 넣기
			String proname=multi.getParameter("proname");
			int proPrice=Integer.parseInt(multi.getParameter("proPrice"));
			//integer 파싱용
			int proSale=0;
			if(multi.getParameter("prosale")==null||multi.getParameter("prosale").equals("")) {
				proSale=0;
			}else {
				proSale=Integer.parseInt(multi.getParameter("prosale"));
			}
			String prokind=multi.getParameter("prokind");
			System.out.println(prokind);
			String proDate=multi.getParameter("proDate");
			String proPhoto=multi.getFilesystemName("proPhoto");
			String proContent=multi.getParameter("proContent");

			
		//3. 변수값을 데이터베이스에 넣기
			
			AdminRepository adminRepository=AdminRepository.getInstance();
			int result=adminRepository.proSave(proname, proPrice, proSale, prokind, proDate, proPhoto, proContent);
			System.out.println(result);
			
		//4. 값을 데이터베이스에 넣는데 성공했으면, admin 첫 페이지로 돌아가고, 아니라면 history.back()
			if(result==1) {
				Script.href("등록에 성공하셨습니다.", "admin/adminHome.jsp", response);
			}else {
				Script.back("등록에 실패하였습니다.", response);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(TAG+"로직 오류");
			// TODO: handle exception
		}
	}

}
