package com.zorvyn.financedashboard.services.impl;

import com.zorvyn.financedashboard.dtos.FinanceRecordRequest;
import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.dtos.FinanceSummaryDto;
import com.zorvyn.financedashboard.entities.Category;
import com.zorvyn.financedashboard.entities.FinanceRecord;
import com.zorvyn.financedashboard.entities.RecordType;
import com.zorvyn.financedashboard.entities.User;
import com.zorvyn.financedashboard.exception.ResponseStatus;
import com.zorvyn.financedashboard.exception.custom.ResourceNotFoundException;
import com.zorvyn.financedashboard.mapper.FinanceRecordMapper;
import com.zorvyn.financedashboard.repositories.CategoryRepository;
import com.zorvyn.financedashboard.repositories.FinanceRecordRepository;
import com.zorvyn.financedashboard.repositories.FinanceRecordSpecification;
import com.zorvyn.financedashboard.services.FinanceRecordService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinanceRecordServiceImpl implements FinanceRecordService {

    private final FinanceRecordRepository recordRepository;
    private final CategoryRepository categoryRepository;
    private final FinanceRecordMapper recordMapper;

    @Override
    @Transactional
    public FinanceRecordResponse createRecord(FinanceRecordRequest request, User user) {
        FinanceRecord record = new FinanceRecord();
        record.setAmount(request.amount());
        record.setType(request.type());
        record.setDescription(request.description());
        record.setTransactionDate(request.transactionDate());
        record.setUser(user);

        Category category = resolveCategory(request.categoryId(), user);
        record.setCategory(category);

        return recordMapper.toResponse(recordRepository.save(record));
    }

    @Override
    public List<FinanceRecordResponse> getFilteredRecords(
            User user, RecordType type, Integer categoryId, LocalDate start, LocalDate end) {

        Specification<FinanceRecord> spec = Specification
                .where(FinanceRecordSpecification.belongsToUser(user))
                .and(FinanceRecordSpecification.isNotDeleted())
                .and(FinanceRecordSpecification.hasType(type))
                .and(FinanceRecordSpecification.hasCategory(categoryId))
                .and(FinanceRecordSpecification.betweenDates(start, end));

        return recordMapper.toResponseList(recordRepository.findAll(spec));
    }

    @Override
    public FinanceSummaryDto getSummary(User user, Integer categoryId, LocalDate start, LocalDate end) {
        Specification<FinanceRecord> spec = Specification
                .where(FinanceRecordSpecification.belongsToUser(user))
                .and(FinanceRecordSpecification.isNotDeleted())
                .and(FinanceRecordSpecification.hasCategory(categoryId))
                .and(FinanceRecordSpecification.betweenDates(start, end));

        List<FinanceRecord> records = recordRepository.findAll(spec);

        BigDecimal income = records.stream()
                .filter(r -> r.getType() == RecordType.INCOME)
                .map(FinanceRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = records.stream()
                .filter(r -> r.getType() == RecordType.EXPENSE)
                .map(FinanceRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new FinanceSummaryDto(income, expense, income.subtract(expense));
    }

    @Override
    @Transactional
    public void deleteRecord(UUID id, User user) {
        FinanceRecord record = recordRepository.findById(id)
                .filter(r -> r.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Record not found", ResponseStatus.RECORD_NOT_FOUND));

        record.setDeleted(true);
        recordRepository.save(record);
    }

    private Category resolveCategory(Integer categoryId, User user) {
        if (categoryId != null) {
            return categoryRepository.findByIdAndUserAndIsDeletedFalse(categoryId, user)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found", ResponseStatus.CATEGORY_NOT_FOUND));
        }
        return categoryRepository.findByNameAndUserAndIsDeletedFalse("MISCELLANEOUS", user)
                .orElseGet(() -> {
                    Category misc = new Category();
                    misc.setName("MISCELLANEOUS");
                    misc.setUser(user);
                    return categoryRepository.save(misc);
                });
    }

}
