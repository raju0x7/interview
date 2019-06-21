import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import challenge.domain.FailedLoginAttempt;
import challenge.domain.User;
import challenge.exception.AuthenticationException;
import challenge.service.PasswordEncodingService;

@Controller
@RequestMapping(value="/login")
public class LoginController {
	
	@Autowired PasswordEncodingService passwordEncoder;
	@Autowired EntityManager em; 

	@RequestMapping(method=RequestMethod.GET)
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		
		return "login";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String authenticate(@RequestParam(value="username", required=true) String username, 
								@RequestParam(value="password", required=true) String password,
								HttpSession session,
								Model model,
								RedirectAttributes redir) 
								throws AuthenticationException {
		User user;
		Long failedAttempts;
		
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);
		final Query failedAttemptsQ = em.createQuery("select COUNT(f) from FailedLoginAttempt f where f.username = :username and f.failedAt >= :from");
		failedAttemptsQ.setParameter("username", username);
		failedAttemptsQ.setParameter("from", calendar.getTime());
		failedAttempts = (Long) failedAttemptsQ.getSingleResult();
		
		if (failedAttempts >= 3) {
			redir.addFlashAttribute("errorMessage", "Username is locked.");
			redir.addFlashAttribute("username", username);
			return "redirect:/login";
		}
		
		try {
			final String encoded = passwordEncoder.encode(password);
			final Query query = em.createQuery("select u from User u where u.username = :username and u.password = :encoded)");
			query.setParameter("username", username);
			query.setParameter("encoded", encoded);
			user = (User) query.getSingleResult();
		}
		catch (Exception nre) { 
			FailedLoginAttempt failed = new FailedLoginAttempt();
			failed.setUsername(username);
			failed.setOcurrence(new Date());
			
			redir.addFlashAttribute("errorMessage", "Bad credentials.");
			redir.addFlashAttribute("username", username);
			return "redirect:/login";
		}
		
		session.setAttribute("user", user);
		session.setAttribute("principal", user.getUsername());
		
		return "redirect:/discussions?sessionId="+session.getId();

	}

}
