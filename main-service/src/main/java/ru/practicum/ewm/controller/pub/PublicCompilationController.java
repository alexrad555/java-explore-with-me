package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.controller.dto.CompilationDto;
import ru.practicum.ewm.service.CompilationService;

import java.util.List;

@RequiredArgsConstructor
@RestControllerAdvice
@RestController
@RequestMapping("compilations")
public class PublicCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> findAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
       return compilationService.findAll(pinned, from, size);
    }

    @GetMapping("{compId}")
    public CompilationDto findById(@PathVariable Long compId) {
        return compilationService.findDtoById(compId);
    }
}
