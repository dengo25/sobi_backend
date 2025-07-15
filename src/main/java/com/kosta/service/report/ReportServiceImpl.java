// 5. 완성된 ReportServiceImpl
package com.kosta.service.report;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;
import com.kosta.dto.report.ReportDto;
import com.kosta.dto.report.ReportListDto;
import com.kosta.dto.report.ReportSearchDto;
import com.kosta.dto.report.ReportPageResponse;
import com.kosta.repository.admin.AdminRepository;
import com.kosta.repository.report.ReportRepository;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final AdminRepository adminRepository;
    
    @Override
    @Transactional
    public void saveReport(ReportDto dto) {
        Member reporterId = adminRepository.findByMemberId(dto.getReporterId());
        Member reportedId = adminRepository.findByMemberId(dto.getReportedId());
        
        Report report = new Report();
        report.setReporterId(reporterId);
        report.setReportedId(reportedId);
        report.setDetail(dto.getDetail());
        report.setReportType(dto.getReportType());
        report.setTargetId(dto.getTargetId());
        report.setStatus("PENDING");
        reportRepository.save(report);
    }
    
    @Override
    public List<ReportListDto> getReportList(String status, String type) {
        // 기존 메서드 구현 (페이징 없이)
        List<Report> reports = reportRepository.findByStatusAndType(status, type);
        return reports.stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public ReportPageResponse getReportsWithFiltersAndPaging(ReportSearchDto searchDto) {
        // 1. 정렬 조건 설정
        Sort sort = createSort(searchDto.getSortBy(), searchDto.getSortDir());
        
        // 2. 페이징 정보 설정
        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), sort);
        
        // 3. 필터 조건으로 데이터베이스 조회
        Page<Report> reportPage = reportRepository.findReportsWithFilters(
            searchDto.getReporterId(),
            searchDto.getReportedId(),
            searchDto.getStatus(),
            searchDto.getReportType(),
            pageable
        );
        
        // 4. Entity를 DTO로 변환
        List<ReportListDto> reportDtos = reportPage.getContent()
            .stream()
            .map(this::convertToListDto)
            .collect(Collectors.toList());
        
        // 5. 페이징 응답 객체 생성
        return ReportPageResponse.of(
            reportDtos,
            reportPage.getTotalElements(),
            reportPage.getNumber(),
            reportPage.getSize()
        );
    }
    
    private Sort createSort(String sortBy, String sortDir) {
        // ReportListDto에 있는 필드만 정렬 허용
        if (!isValidSortField(sortBy)) {
            sortBy = "createdAt"; // 기본값으로 설정
        }
        
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }
    
    /**
     * 유효한 정렬 필드인지 검증
     */
    private boolean isValidSortField(String sortBy) {
        return sortBy != null && (
            "reporterId".equals(sortBy) ||
            "reportedId".equals(sortBy) ||
            "reportType".equals(sortBy) ||
            "status".equals(sortBy) ||
            "createdAt".equals(sortBy)
        );
    }
    
    private ReportListDto convertToListDto(Report report) {
        return ReportListDto.builder()
        	.reportId(report.getReportId())
            .reporterId(report.getReporterId().getMemberId())  // Member에서 ID 추출
            .reportedId(report.getReportedId().getMemberId())  // Member에서 ID 추출
            .reportType(report.getReportType())
            .status(report.getStatus())
            .createdAt(report.getCreatedAt())
            .targetId(report.getTargetId())
            .build();
    }

}