import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import com.securecodewarrior.employee.model.request.EmployeeRequestVO;
import com.securecodewarrior.employee.processors.EmployeeXmlProcessors;
import com.securecodewarrior.employee.service.EmployeeService;

public class EmployeeServiceImpl implements EmployeeService {

	private static Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Override
	public List<EmployeeRequestVO> uploadEmployeeUsingXml(MultipartFile file) {
		List<EmployeeRequestVO> employees = null;
		try {
			SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = saxParserFactory.newSAXParser();
			EmployeeXmlProcessors handler = new EmployeeXmlProcessors();
			saxParser.parse(file.getInputStream(), handler);
			//Get Employees list
			employees = handler.getEmpList();
			logger.info("Employee parsed successfully");
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return employees;

	}
}
