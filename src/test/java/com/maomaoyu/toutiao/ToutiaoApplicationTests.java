package com.maomaoyu.toutiao;

import com.maomaoyu.toutiao.bean.*;
import com.maomaoyu.toutiao.mapper.*;
import com.maomaoyu.toutiao.service.UserService;
import com.maomaoyu.toutiao.util.MailSenderUtil;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ToutiaoApplicationTests {

	@Autowired
	private VelocityEngine velocityEngine;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	UserMapper userMapper;

	@Autowired
	NewsMapper newsMapper;

	@Autowired
	UserService userService;

	@Autowired
	LoginTicketMapper loginTicketMapper;

	@Autowired
	CommentMapper commentMapper;

	@Autowired
    MailSenderUtil mailSenderUtil;

	@Autowired
	MessageMapper messageMapper;

	@Test
	public void contextLoads() {
	}

	@Test
	public void addTest(){
		Random random = new Random();

		for (int i = 0; i < 11; i++) {
			User user = new User();
			user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png",random.nextInt(10000)));
			user.setName(String.format("User%d",i));
			user.setPassword("");
			user.setSalt("");
			user.setState(0);
			userMapper.addUser(user);
		}
	}

	@Test
	public void updateTest(){
		for (int i = 0; i < 11; i++) {
			User user = new User();
			user.setId(i);
			user.setPassword("newpassword");
			userMapper.updatePassword(user);
		}
		Assert.assertEquals("newpassword",userMapper.selectById(1).getPassword());

	}

	@Test
	public void test1(){
		System.out.println(userMapper.selectById(1));
	}

	@Test
	public void addNewsTest(){
		Random random = new Random();
		for (int i = 0; i < 11; i++) {
			News news = new News();
			news.setCommentCount(i);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			news.setCreatedDate(date);
			news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
			news.setLikeCount(i+1);
			news.setUserId(i+1);
			news.setTitle(String.format("TITLE{%d}", i));
			news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
			news.setState(1);
			newsMapper.addNews(news);
		}
	}

	@Test
	public void test2(){

		System.out.println(newsMapper.selectByUserIdAndOffset(0,0,10));
	}

	@Test
	public void test3(){
		String username = "maomaoyu";
		String password = "123";
		//userService.register(username,password);
	}

	@Test
	public void test4(){
		Random random = new Random();
		for (int i = 0; i < 11; i++) {
			LoginTicket ticket = new LoginTicket();
			ticket.setUserId(i+1);
			ticket.setStatus(0);
			Date date = new Date();
			date.setTime(date.getTime() + 1000*3600*5*i);
			ticket.setExpired(date);
			ticket.setTicket(String.format("TICKET%d", i+1));
			loginTicketMapper.addTicket(ticket);
			loginTicketMapper.updateStatus(ticket.getTicket(),2);
		}
	}

	@Test
	public void test5(){
		System.out.println(userService.login("maomaoyu","123"));
	}

	@Test
	public void test6(){
		// 给每个资讯插入3个评论
		for (int i = 13;i < 72;i++) {
			for (int j = 0; j < 13; ++j) {
				Comment comment = new Comment();
				comment.setUserId(j+1);
				comment.setCreatedDate(new Date());
				comment.setStatus(0);
				comment.setContent("这里是一个评论啊！" + String.valueOf(j));
				comment.setEntityId(i);
				comment.setEntityType(EntityType.ENTITY_NEWS);
				commentMapper.addComment(comment);
			}
		}
	}

	@Test
	public void test7(){
		for (int i = 67; i < 72 ; i++) {
			News news = newsMapper.getById(i);
			int count = commentMapper.getCommentCount(i,EntityType.ENTITY_NEWS);
			news.setCommentCount(count);
			newsMapper.updateNews(news);
		}
	}

	@Test
	public void test8(){
		Map<String,Object> map = new HashMap<>();
		map.put("username","ermaoyu");
		mailSenderUtil.sendWithHTMLTemplate("871365987@qq.com","hello","mails/welcome.html",map);
	}

	@Test
	public void test9(){
		String nick = null;
		try {
			nick = MimeUtility.encodeText("毛毛雨");
			InternetAddress form = new InternetAddress(nick + "<928432997@qq.com>");
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			String res = "验证码为:" + new Random().nextInt();
			mimeMessageHelper.setTo("871365987@qq.com");
			mimeMessageHelper.setSubject("hellow");
			mimeMessageHelper.setFrom(form);
			mimeMessageHelper.setText(res,true);
			mailSender.send(mimeMessage);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void updateMessageTest(){
		int id = 1;
		messageMapper.updataMessage(id);
	}
}
