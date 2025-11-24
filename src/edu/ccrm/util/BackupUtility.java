package edu.ccrm.util;

import edu.ccrm.io.ImportExportService;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BackupUtility {
    private ImportExportService importExportService;
    
    public BackupUtility(ImportExportService importExportService) {
        this.importExportService = importExportService;
    }
    
    public void createBackup(String backupDir) throws IOException {
        Path backupPath = Paths.get(backupDir);
        if (!Files.exists(backupPath)) {
            Files.createDirectories(backupPath);
        }
        
        // Create timestamped folder
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        Path timestampedPath = backupPath.resolve("backup_" + timestamp);
        
        try {
            Files.createDirectory(timestampedPath);
        } catch (FileAlreadyExistsException e) {
            // Handle case where directory already exists (unlikely with timestamp)
            throw new IOException("Backup directory already exists: " + timestampedPath);
        }
        
        // Export data to the backup folder
        importExportService.exportStudentsToCSV(timestampedPath.resolve("students.csv").toString());
        importExportService.exportCoursesToCSV(timestampedPath.resolve("courses.csv").toString());
        
        System.out.println("Backup created successfully at: " + timestampedPath);
    }
    
    public long calculateBackupSize(String backupDir) throws IOException {
        Path backupPath = Paths.get(backupDir);
        if (!Files.exists(backupPath)) {
            return 0;
        }
        
        return Files.walk(backupPath)
                .filter(Files::isRegularFile)
                .mapToLong(p -> {
                    try {
                        return Files.size(p);
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .sum();
    }
}