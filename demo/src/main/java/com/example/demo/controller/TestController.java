package com.example.demo.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.Search_channel;
 
@Controller
@EnableAutoConfiguration
public class TestController {
 
  @RequestMapping(value="/")
  public String main() {
    return "index";
  }
   
  @RequestMapping(value="/hello")
  public String hellSpringBoot( Model model ) {
	  model.addAttribute("post", "test");
	  return "hello";
  }
  
  @RequestMapping(value = "/test", method = RequestMethod.GET)
  public @ResponseBody String test() throws Exception{
	  
	  String val = Search_channel.startSearch();
      return val;
  }
  
}


