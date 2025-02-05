package org.example.feedback.mapper.index;

import org.example.feedback.dto.response.InboxResponse;
import org.example.feedback.domain.model.Inbox;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface InboxMapper {

    InboxMapper INSTANCE = Mappers.getMapper(InboxMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "topic", target = "topic")
    @Mapping(source = "expirationDate", target = "expirationDate")
    @Mapping(source = "allowAnonymous", target = "allowAnonymous")
    InboxResponse inboxToInboxResponse(Inbox inbox);

    List<InboxResponse> inboxesToResponse(List<Inbox> inboxes);
}
