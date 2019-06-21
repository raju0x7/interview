// Find vulnerability in the below code?

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
