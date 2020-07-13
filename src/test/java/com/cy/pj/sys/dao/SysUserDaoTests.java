package com.cy.pj.sys.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cy.pj.sys.vo.SysUserDeptVo;

@SpringBootTest
public class SysUserDaoTests {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Test
	public void testGetRowCount() {
		int rowCount=sysUserDao.getRowCount(null);
		System.out.println("rowCount="+rowCount);
	}
	@Test
	public void testFindPageObjects() {
		List<SysUserDeptVo> list=
		sysUserDao.findPageObjects(null, 0, 5);
		list.forEach((u)->System.out.println(u));
		
	}
}









