package com.pnu.dev.pnufeedback.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class StakeholderCategory {

    @Id
    private Long id;

    private String title;

    private boolean showInReport;

}
