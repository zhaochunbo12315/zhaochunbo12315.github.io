package com.cy.pj.sys.dao;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SysMenuDaoTests {
	   @Autowired
	   private SysMenuDao sysMenuDao;
	   @Test
	   public void testFindObjects() {
		   List<Map<String,Object>> list=sysMenuDao.findObjects();
		   //Assertions.assertNotEquals(null, list);//断言测试(了解)
		   System.out.println("list.size="+list.size());
		   for(Map<String,Object> map:list) {
			 System.out.println(map);
		   }
		   //jdk8 lambda表达式 了解
		   //list.forEach((m)->System.out.println(m));
	   }
}//3:15继续
