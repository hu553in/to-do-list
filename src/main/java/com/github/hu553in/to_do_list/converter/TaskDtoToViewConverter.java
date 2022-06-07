package com.github.hu553in.to_do_list.converter;

import com.github.hu553in.to_do_list.dto.TaskDto;
import com.github.hu553in.to_do_list.mapper.TaskMapper;
import com.github.hu553in.to_do_list.view.TaskView;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TaskDtoToViewConverter implements Converter<TaskDto, TaskView> {

    @Override
    public TaskView convert(@NonNull final TaskDto source) {
        return TaskMapper.INSTANCE.mapDtoToView(source);
    }

}
