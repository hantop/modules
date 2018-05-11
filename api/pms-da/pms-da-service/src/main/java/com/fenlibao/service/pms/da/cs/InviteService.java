package com.fenlibao.service.pms.da.cs;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.fenlibao.model.pms.da.cs.Invite;
import com.fenlibao.model.pms.da.cs.form.PageForm;

public interface InviteService {
	List<Invite> inviteList(PageForm pageForm,RowBounds bounds);
}
