package wl.action;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.n3r.eql.EqlPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wl.service.PrintDetailService;

import com.alibaba.fastjson.JSON;

@Controller
public class PrintDetailController {
    @Autowired
    @Qualifier("printDetailService")
    private PrintDetailService printDetailService;
	
	@RequestMapping(value = "/toPrintDetail", method = RequestMethod.GET)
    public String toSelectPrintDetail(String subscribeId,Model model,HttpServletRequest request) {

        Map map=printDetailService.selectDetailInfo(subscribeId);

		model.addAttribute("results", map);
		return "pages/printDetail";
    }
}
