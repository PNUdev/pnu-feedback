package com.pnu.dev.pnufeedback.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportDetailedStatistics {

    private Map<Long, Long> submissionsCountByStakeholderCategory; // stakeholderCategoryId -> submissionsCount

    private List<QuestionDetailedStatistics> questionDetailedStatistics;

}
