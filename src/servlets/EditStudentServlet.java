package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;


import com.RapidFeedback.MysqlFunction;
import com.RapidFeedback.Student;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName EditStudentServlet
 * @Description Servlet to edit a student information in a specific project in
 *              DB.
 *
 * @author Jingxian Hu, Zhongke Tan, Xizhi Geng
 */
@WebServlet("/EditStudentServlet")
public class EditStudentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditStudentServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ")
				.append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		MysqlFunction dbFunction = new MysqlFunction();
		// get JSONObject from request
		JSONObject jsonReceive;
		BufferedReader reader = request.getReader();
		String str, wholeString = "";
		while ((str = reader.readLine()) != null) {
			wholeString += str;
		}
		System.out.println("Receive: " + wholeString);
		jsonReceive = JSON.parseObject(wholeString);

		// get values from received JSONObject
		String token = jsonReceive.getString("token");
		int studentId = jsonReceive.getIntValue("studentId");
		int studentNumber = jsonReceive.getIntValue("studentNumber");
		String firstName = jsonReceive.getString("firstName");
		String middleName = jsonReceive.getString("middleName");
		String lastName = jsonReceive.getString("lastName");
		String email = jsonReceive.getString("email");
		int group = jsonReceive.getIntValue("group");
		int projectId = jsonReceive.getIntValue("projectId");

		ServletContext servletContext = this.getServletContext();
		int loginId = Token.tokenToUser(servletContext, token);
		boolean updateStudent_ACK = false;
		
//		if (dbFunction.isMarkerPrincipal(loginId)) {
		
		// Mention:
		// call the SQL method to edit the student information 
		// return the 'true' or 'false' value to updateStudent_ACK
		updateStudent_ACK = dbFunction.updateStudent(studentId, studentNumber, firstName,
				lastName, middleName, email, group, projectId);
//		}

		// construct the JSONObject to send
		JSONObject jsonSend = new JSONObject();
		jsonSend.put("updateStudent_ACK", updateStudent_ACK);

		// send
		PrintWriter output = response.getWriter();
		output.print(jsonSend.toJSONString());
	}
}
