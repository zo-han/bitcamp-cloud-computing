package bitcamp.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

//파일업로드 방법2 - Servlet - 방법2 xml설정 
// Servlet 3.0 부터 multipart data를 처리하는 API를 제공한다.
// 일반데이터는 쓰던데로 파라미터에서 꺼내면 되고, 데이터만 Part를 통해 write하면 편하다.
// multipart data를 처리하는 Servlet은 
// 처리 정보를 Annotation 또는 DD파일(web.xml; Deployment Descriptor 파일) 에 설정해야 한다.

// => 방법2 web.xml 로 설정
public class FileUpaloadServlet05 extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        
        InputStream fileContent = null;
        OutputStream fileOut = null;

        try {
            String name = req.getParameter("name");
            String age = req.getParameter("age");
            Part photo = req.getPart("photo");
            
            //새 파일명 준비
            String newfilename = UUID.randomUUID().toString();
            String path = req.getServletContext().getRealPath("/files/" + newfilename);
            
            // Part 데이터를 저장한다.
            photo.write(path);
            
            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.println("<html><head><title>파일 업로드</title></head><body>");
            out.printf("name = %s<br>\n", name);
            out.printf("age = %s<br>\n", age);
            out.printf("photo = <a href='files/%s'/>%s</a><br>\n", newfilename, newfilename);
            out.printf("<img src=/files/%s><br>", newfilename);
            out.printf("<img id='img1' src=/files/%s><br>", newfilename);
            out.println("</body></html>");
            
            out.printf("<script>"+
                    "setTimeout(() => {document.getElementById('img1').src = 'files/%s';}, 5000);"+
                    "</script>", newfilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
