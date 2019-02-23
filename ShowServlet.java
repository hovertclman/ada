package web;

import bean.Book;
import bean.PageBean;
import service.IBookService;
import service.impl.BookServiceImpl;
import utils.C3p0Util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ShowServlet extends HttpServlet {
    IBookService bookService=new BookServiceImpl();
    PageBean pageBean = new PageBean();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");//例行操作没啥用
        Map<String, String[]> map = request.getParameterMap();//虽然只有一个参数,顺便可以重置pagebean里面的currPage
            pageBean = C3p0Util.getBean(PageBean.class, map);
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=UTF-8");//例行操作没啥用
        String currPage = request.getParameter("currPage");//取出当前页信息
        if(pageBean.getCategoryList()==null){//如果类型数据为空则创建
            pageBean.setCategoryList(bookService.querytype());//创建类型集合
        }
        if (currPage!=null){//如果当前页不为空则赋值,为空有默认值1
            pageBean.setCurrPage(Integer.parseInt(currPage));
        }
        List<Book> list=bookService.selectAll(pageBean);
        System.out.println(pageBean);
        HttpSession session = request.getSession();
        session.setAttribute("list",list);
        session.setAttribute("pageBean",pageBean);
        request.getRequestDispatcher("/page/pageShow.jsp").include(request,response);
    }
}
