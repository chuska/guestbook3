package com.bit2015.guestbook3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bit2015.guestbook3.dao.GuestbookDao;
import com.bit2015.guestbook3.vo.GuestbookVo;

@Controller
public class GuestBookController {
	@Autowired
	@Qualifier("gbDao")
	GuestbookDao guestbookDao;

	@RequestMapping("/")
	public String index(Model model) {
		List<GuestbookVo> list = guestbookDao.getList();
		model.addAttribute("list", list);
		return "/WEB-INF/views/index.jsp";
	}

	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insert(@ModelAttribute GuestbookVo vo) {
		guestbookDao.insert(vo);
		return "redirect:/";
	}

	@RequestMapping("/deleteform")
	public String deleteform(Model model, @RequestParam Long no) {
		model.addAttribute("no", no);
		return "/WEB-INF/views/deleteform.jsp";
	}

	@RequestMapping("/delete")
	public String delete(@ModelAttribute GuestbookVo vo) {
		guestbookDao.delete(vo);
		return "redirect:/";
	}
}
