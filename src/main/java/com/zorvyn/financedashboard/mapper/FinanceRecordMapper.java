package com.zorvyn.financedashboard.mapper;

import com.zorvyn.financedashboard.dtos.FinanceRecordResponse;
import com.zorvyn.financedashboard.entities.FinanceRecord;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface FinanceRecordMapper {

    FinanceRecordResponse toResponse(FinanceRecord entity);

    List<FinanceRecordResponse> toResponseList(List<FinanceRecord> entities);

}
