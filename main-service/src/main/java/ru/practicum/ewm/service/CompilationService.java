package ru.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.OffsetPageable;
import ru.practicum.ewm.controller.dto.CompilationDto;
import ru.practicum.ewm.controller.dto.EventShortDto;
import ru.practicum.ewm.controller.dto.NewCompilationDto;
import ru.practicum.ewm.controller.dto.UpdateCompilationRequest;
import ru.practicum.ewm.exception.DataNotFoundException;
import ru.practicum.ewm.mapper.EventMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.EventSearchParam;
import ru.practicum.ewm.repository.CompilationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventService eventService;
    private final EventMapper eventMapper;

    public CompilationDto create(NewCompilationDto compilationDto) {
        Compilation compilation = new Compilation();
        compilation.setPinned(compilationDto.isPinned());
        compilation.setTitle(compilationDto.getTitle());
        if (compilationDto.getEvents() != null) {
            compilation.setEvents(eventService.findAll(compilationDto.getEvents()));
        } else {
            compilation.setEvents(Collections.EMPTY_LIST);
        }
        compilationRepository.save(compilation);
        return compilationToDto(compilation);
    }

    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        Pageable pageable = new OffsetPageable(from, size, Sort.by(Sort.Direction.DESC, "id"));
        List<Compilation> compilations;
        compilations = pinned == null ? compilationRepository.findAll(pageable).toList()
                : compilationRepository.findAllByPinned(pinned, pageable);
        return compilations.stream()
                .map(this::compilationToDto)
                .collect(Collectors.toList());
    }

    public CompilationDto findDtoById(Long compId) {
        return compilationToDto(findById(compId));
    }

    public Compilation findById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(
                        () -> new DataNotFoundException(String.format("Подборка с id %d не найден", compId)));
    }

    public void deleteById(Long compId) {
        findById(compId);
        compilationRepository.deleteById(compId);
    }

    public CompilationDto update(Long compId, UpdateCompilationRequest updateCompilationDto) {
        Compilation compilation = findById(compId);
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getTitle() != null) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        if (updateCompilationDto.getEvents() != null) {
            compilation.setEvents(eventService.findAll(updateCompilationDto.getEvents()));
        }
        compilationRepository.save(compilation);
        return compilationToDto(compilation);
    }

    private CompilationDto compilationToDto(Compilation compilation) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        if (compilation.getEvents() != null && !compilation.getEvents().isEmpty()) {
            EventSearchParam eventSearchParam = new EventSearchParam(compilation.getEvents().stream()
                    .map(Event::getId)
                    .collect(Collectors.toList()), false);
            eventShortDtos = eventMapper.toShortDto(eventService.findAll(eventSearchParam));
        }
        return new CompilationDto(compilation.getId(),
                eventShortDtos, compilation.isPinned(), compilation.getTitle());
    }
}
