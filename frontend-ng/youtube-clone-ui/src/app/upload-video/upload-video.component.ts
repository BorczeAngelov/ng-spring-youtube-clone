import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgxFileDropEntry, FileSystemFileEntry, FileSystemDirectoryEntry } from 'ngx-file-drop';
import { VideoService } from '../video.service';


@Component({
  selector: 'app-upload-video',
  templateUrl: './upload-video.component.html',
  styleUrls: ['./upload-video.component.css']
})
export class UploadVideoComponent {

  public files: NgxFileDropEntry[] = [];
  public fileUploaded: boolean = false;
  private fileEntry: FileSystemFileEntry | undefined;

  constructor(private videoService: VideoService, private router: Router) { }

  public dropped(files: NgxFileDropEntry[]) {
    this.files = files;
    for (const droppedFile of files) {

      // Is it a file?
      if (droppedFile.fileEntry.isFile) {

        this.fileEntry = droppedFile.fileEntry as FileSystemFileEntry;
        this.fileEntry.file((file: File) => {

          console.log(droppedFile.relativePath, file);
          this.fileUploaded = true;

        });
      } else {
        // It was a directory (empty directories are added, otherwise only files)
        const fileEntry = droppedFile.fileEntry as FileSystemDirectoryEntry;
        console.log(droppedFile.relativePath, fileEntry);
      }
    }
  }

  public fileOver(event: any) {
    console.log(event);
  }

  public fileLeave(event: any) {
    console.log(event);
  }

  public uploadVideo() {
    if (this.fileEntry) {


      this.fileEntry.file(file => {

        this.videoService.uploadVideo(file).subscribe(
          data => {
            console.log("Video uploaded successfully", data);
            
            this.router.navigateByUrl("/save-video-details/" + data.videoId);
          })

      })
    }
  }

}
