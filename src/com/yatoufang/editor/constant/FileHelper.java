package com.yatoufang.editor.constant;

import com.yatoufang.editor.Model;
import com.yatoufang.service.NotifyService;
import com.yatoufang.templet.NotifyKeys;
import com.yatoufang.utils.FileWrite;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


/**
 * @author hse
 * @since 2022/9/23 0023
 */
public final class FileHelper {

    public static void save(Model model) {
        String filePath = model.getFilePath();
        if (filePath == null) {
            saveAs(model);
        } else {
            save(model, filePath);
        }
    }

    public static void saveAs(Model model) {
        String selectPath = FileWrite.getSelectedPath();
        if(selectPath != null){
            save(model, selectPath);
        }
    }

    private static void save(Model model, String path) {
        try {
            model.setFilePath(path);
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(model);
            oos.close();
            NotifyService.notify(NotifyKeys.SAVED);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(BufferedImage image, Model model){
        String selectPath = FileWrite.getSelectedPath();
        if(selectPath != null){
            File file = new File(selectPath + "\\" +model.getModuleName() + ".png");
            try {
                ImageIO.write(image, "png", file);
                NotifyService.notify(NotifyKeys.SAVED);
            } catch (IOException error) {
                error.printStackTrace();
            }
        }
    }
}