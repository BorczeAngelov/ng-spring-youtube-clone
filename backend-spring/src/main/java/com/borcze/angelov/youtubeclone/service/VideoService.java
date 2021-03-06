package com.borcze.angelov.youtubeclone.service;

import com.borcze.angelov.youtubeclone.dto.UploadVideoResponse;
import com.borcze.angelov.youtubeclone.dto.VideoDto;
import com.borcze.angelov.youtubeclone.model.Video;
import com.borcze.angelov.youtubeclone.repository.VideoRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class VideoService {

    private final S3Service s3Service;
    private final VideoRepository videoRepository;

    public VideoService(S3Service s3Service, VideoRepository videoRepository) {
        this.s3Service = s3Service;
        this.videoRepository = videoRepository;
    }

    public UploadVideoResponse uploadVideo(MultipartFile file) {
        var videoUrl = s3Service.uploadFile(file);

        var video = new Video();
        video.setUrl(videoUrl);

        var savedVideo = videoRepository.save(video);
        return new UploadVideoResponse(savedVideo.getId(), savedVideo.getUrl());
    }

    public VideoDto editVideo(VideoDto videoDto) {
        var savedVideo = getVideoById(videoDto.getVideoId());

        // Map the video Dto fileds to video
        savedVideo.setTitle(videoDto.getVideoName());
        savedVideo.setDescription(videoDto.getDescription());
        savedVideo.setUrl(videoDto.getUrl());
        savedVideo.setTags(videoDto.getTags());
        savedVideo.setVideoStatus(videoDto.getVideoStatus());

        videoRepository.save(savedVideo);
        return videoDto;
    }

    public String uploadThumbnail(MultipartFile file, String videoId) {

        var video = getVideoById(videoId);
        var thumbnailUrl = this.s3Service.uploadFile(file);

        video.setThumbnailUrl(thumbnailUrl);
        videoRepository.save(video);

        return thumbnailUrl;
    }

    private Video getVideoById(String videoId) {
        return videoRepository.findById(videoId)
                .orElseThrow(() -> new IllegalArgumentException("Cannot find video by id - " + videoId));
    }
}
