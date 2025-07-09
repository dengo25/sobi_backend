package com.kosta.repository.report;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.kosta.domain.member.Member;
import com.kosta.domain.report.Report;
import com.kosta.repository.member.MemberRepository;
import com.kosta.repository.report.ReportRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ReportRepositoryTest {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("신고 생성 테스트")
    public void createReportTest() {
        // Given: reporter, reported Member 저장
        Member reporter = new Member();
        reporter.setMemberId("신고자");
        memberRepository.save(reporter);

        Member reported = new Member();
        reported.setMemberId("피신고자");
        memberRepository.save(reported);

        // When: Report 저장
        Report report = new Report();
        report.setReporterId(reporter);
        report.setReportedId(reported);
        report.setReportType("후기 신고");
        report.setTargetId(123);
        report.setDetail("욕설이 포함되어 있습니다.");

        Report savedReport = reportRepository.save(report);

        // Then: 저장된 값 확인
        Optional<Report> found = reportRepository.findById(savedReport.getReportId());
        assertThat(found).isPresent();
        assertThat(found.get().getReporterId().getMemberId()).isEqualTo("신고자");
        assertThat(found.get().getReportedId().getMemberId()).isEqualTo("피신고자");
        assertThat(found.get().getStatus()).isEqualTo("PENDING");
        assertThat(found.get().getCreatedAt()).isNotNull();
    }
}
