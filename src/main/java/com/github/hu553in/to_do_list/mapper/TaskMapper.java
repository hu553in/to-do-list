package com.github.hu553in.to_do_list.mapper;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.entity.TaskEntity;
import com.github.hu553in.to_do_list.view.TaskView;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(uses = UserMapper.class)
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto mapEntityToDto(TaskEntity source);

    TaskView mapDtoToView(TaskDto source);

}
