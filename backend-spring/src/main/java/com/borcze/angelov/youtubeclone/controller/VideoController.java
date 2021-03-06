package com.borcze.angelov.youtubeclone.controller;

import com.borcze.angelov.youtubeclone.dto.UploadVideoResponse;
import com.borcze.angelov.youtubeclone.dto.VideoDto;
import com.borcze.angelov.youtubeclone.service.VideoService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UploadVideoResponse uploadVideo(@RequestParam("file") MultipartFile file) {
        return videoService.uploadVideo(file);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public VideoDto editVideoMetadadta(@RequestBody VideoDto videoDto) {
        return this.videoService.editVideo(videoDto);
    }

    @PostMapping("/thumbnail")
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadThumbnail(
            @RequestParam("file") MultipartFile file,
            @RequestParam("videoId") String videoId) {

        return videoService.uploadThumbnail(file, videoId);
    }
}
