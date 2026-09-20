package com.incubyte.acme.analytics;

import com.incubyte.acme.analytics.dto.SalaryByGroupProjection;
import com.incubyte.acme.analytics.dto.SalaryDistributionProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock
    private AnalyticsRepository analyticsRepository;

    @InjectMocks
    private AnalyticsService analyticsService;

    @Test
    void shouldReturnAnalyticsSummaryData() {
        when(analyticsRepository.countEmployees()).thenReturn(10000L);
        when(analyticsRepository.findAverageSalary())
                .thenReturn(new BigDecimal("100000"));
        when(analyticsRepository.findHighestSalary())
                .thenReturn(new BigDecimal("149990"));
        when(analyticsRepository.findLowestSalary())
                .thenReturn(new BigDecimal("50000"));

        assertEquals(10000L, analyticsService.getEmployeeCount());
        assertEquals(
                new BigDecimal("100000"),
                analyticsService.getAverageSalary());
        assertEquals(
                new BigDecimal("149990"),
                analyticsService.getHighestSalary());
        assertEquals(
                new BigDecimal("50000"),
                analyticsService.getLowestSalary());
    }

    @Test
    void shouldReturnSalaryByCountry() {
        SalaryByGroupProjection india = mock(SalaryByGroupProjection.class);

        when(india.getGroupName()).thenReturn("India");
        when(india.getEmployeeCount()).thenReturn(2000L);
        when(india.getAverageSalary())
                .thenReturn(new BigDecimal("99985"));

        when(analyticsRepository.findSalaryByCountry())
                .thenReturn(List.of(india));

        List<SalaryByGroupProjection> result = analyticsService.getSalaryByCountry();

        assertEquals(1, result.size());
        assertEquals("India", result.get(0).getGroupName());
        assertEquals(2000L, result.get(0).getEmployeeCount());
        assertEquals(
                new BigDecimal("99985"),
                result.get(0).getAverageSalary());
    }

    @Test
    void shouldReturnSalaryByDepartment() {
        SalaryByGroupProjection engineering = mock(SalaryByGroupProjection.class);

        when(engineering.getGroupName()).thenReturn("Engineering");
        when(engineering.getEmployeeCount()).thenReturn(1667L);

        when(analyticsRepository.findSalaryByDepartment())
                .thenReturn(List.of(engineering));

        List<SalaryByGroupProjection> result = analyticsService.getSalaryByDepartment();

        assertEquals(1, result.size());
        assertEquals(
                "Engineering",
                result.get(0).getGroupName());
        assertEquals(
                1667L,
                result.get(0).getEmployeeCount());
    }

    @Test
    void shouldReturnSalaryDistribution() {
        SalaryDistributionProjection distribution = mock(SalaryDistributionProjection.class);

        when(distribution.getSalaryRange()).thenReturn("0-75K");
        when(distribution.getEmployeeCount()).thenReturn(2500L);

        when(analyticsRepository.findSalaryDistribution())
                .thenReturn(List.of(distribution));

        List<SalaryDistributionProjection> result = analyticsService.getSalaryDistribution();

        assertEquals(1, result.size());
        assertEquals(
                "0-75K",
                result.get(0).getSalaryRange());
        assertEquals(
                2500L,
                result.get(0).getEmployeeCount());
    }
}