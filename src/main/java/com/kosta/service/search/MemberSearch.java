package com.kosta.service.search;

import org.springframework.data.domain.Page;
import com.kosta.dto.admin.MemberListDto;
import com.kosta.dto.admin.PageRequestDTO;

public interface MemberSearch {
	Page<MemberListDto> searchMemberList(PageRequestDTO pageRequestDTO);
}
