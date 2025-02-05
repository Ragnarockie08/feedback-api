package org.example.feedback.mapper.message;

import org.example.feedback.dto.response.MessageResponse;
import org.example.feedback.domain.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MessageMapper {

    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);

    MessageResponse messageToMessageResponse(Message message);

    List<MessageResponse> messagesToResponse(List<Message> inboxes);

}
