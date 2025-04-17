package com.leaveease.api.repository.impl;

import com.leaveease.api.dto.request.ReportAnalysisRequestDto;
import com.leaveease.api.dto.response.ReportAnalysisResponseDto;
import com.leaveease.api.repository.ReportAnalysisRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ReportAnalysisRepositoryImpl implements ReportAnalysisRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReportAnalysisResponseDto> searchAnalysis(ReportAnalysisRequestDto request) {
        StringBuilder sql = new StringBuilder("SELECT s.staffId, s.staffNumber, s.fullName, s.department, ");
        sql.append("SUM(CASE WHEN la.leaveType = 'Children Leave' THEN DATEDIFF(la.endDate, la.startDate) + 1 ELSE 0 END) as takenChildren, ");
        sql.append("SUM(CASE WHEN la.leaveType = 'Annual Leave' THEN DATEDIFF(la.endDate, la.startDate) + 1 ELSE 0 END) as takenAnnual, ");
        sql.append("SUM(CASE WHEN la.leaveType = 'Sick Leave' THEN DATEDIFF(la.endDate, la.startDate) + 1 ELSE 0 END) as takenSick, ");
        sql.append("SUM(CASE WHEN la.leaveType = 'Emergency Leave' THEN DATEDIFF(la.endDate, la.startDate) + 1 ELSE 0 END) as takenEmergency, ");
        sql.append("SUM(DATEDIFF(la.endDate, la.startDate) + 1) as total ");
        sql.append("FROM LeaveApplication la ");
        sql.append("JOIN Staff s ON s.staffId = la.staffId ");
        sql.append("WHERE la.status = 'Approved' AND la.leaveType IN ('Children Leave', 'Annual Leave', 'Sick Leave', 'Emergency Leave')");

        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            sql.append("AND la.startDate >= :startDate ");
        }
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            sql.append("AND la.endDate <= :endDate ");
        }
        if (request.getDepartment() != null && !request.getDepartment().isEmpty()) {
            sql.append("AND s.department = :department ");
        }
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            sql.append("AND (s.staffNumber LIKE :keyword OR s.fullName LIKE :keyword) ");
        }

        sql.append("GROUP BY s.staffId, s.staffNumber, s.fullName, s.department ");
        sql.append("ORDER BY s.department ASC, s.fullName ASC");

        Query query = entityManager.createNativeQuery(sql.toString());

        // Bind params if present
        if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
            query.setParameter("startDate", request.getStartDate());
        }
        if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
            query.setParameter("endDate", request.getEndDate());
        }
        if (request.getDepartment() != null && !request.getDepartment().isEmpty()) {
            query.setParameter("department", request.getDepartment());
        }
        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            query.setParameter("keyword", "%" + request.getKeyword() + "%");
        }

        List<Object[]> resultList = query.getResultList();
        List<ReportAnalysisResponseDto> results = new ArrayList<>();

        for (Object[] row : resultList) {
            ReportAnalysisResponseDto dto = new ReportAnalysisResponseDto();
            dto.setStaffId((int) row[0]);
            dto.setStaffNumber((String) row[1]);
            dto.setName((String) row[2]);
            dto.setDepartment((String) row[3]);
            dto.setTakenChildren(((Number) row[4]).intValue());
            dto.setTakenAnnual(((Number) row[5]).intValue());
            dto.setTakenSick(((Number) row[6]).intValue());
            dto.setTakenEmergency(((Number) row[7]).intValue());
            dto.setTotal(((Number) row[8]).intValue());
            results.add(dto);
        }

        return results;
    }
}
