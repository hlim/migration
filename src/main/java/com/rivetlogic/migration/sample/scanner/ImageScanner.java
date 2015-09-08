package com.rivetlogic.migration.sample.scanner;

import com.rivetlogic.migration.impl.scanner.ContentScanner;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.regex.Pattern;

/**
 * <p>ImageScanner</p>
 */
public class ImageScanner extends ContentScanner {

    final static Logger LOGGER = Logger.getLogger(ImageScanner.class);

    private Pattern pattern;
    private int targetGroup;
    private String imageRoot;
    private String originalImageRoot;
    private String targetImageFolder;
    private String sourceImageFolder;
    private String replacePattern;
    private int numImagesInFolder;
    private int numFoldersInFolder;
    private int currentNumImages = 1;
    private int currentNumFolders = 1;
    private int currentNumTopFolders = 1;

    @Override
    protected String transform(String front, String target, String end, String match) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("match: " + match);
            LOGGER.debug("front: " + front);
            LOGGER.debug("target: " + target);
            LOGGER.debug("end: " + end);
        }
        String imageName = target.substring(target.lastIndexOf("/") + 1);
        imageName = imageName.replaceAll(getReplacePattern(), "-").toLowerCase();
        String imagePath = getImagePath(imageName);
        moveImage(target, imagePath);
        return front + imagePath + end;
    }

    /**
     * <p>move image from the source location to the target location</p>
     *
     * @param originalImagePath  a {@link java.lang.String} object
     * @param targetImagePath a {@link java.lang.String} object
     */
    private void moveImage(String originalImagePath, String targetImagePath) {
        String imageSubPath = originalImagePath.replaceFirst(getOriginalImageRoot(), "").replaceAll("%20", "\\ ");
        String sourceFullPath = getSourceImageFolder() + "/" + imageSubPath;
        String targetFullPath = getTargetImageFolder() + targetImagePath;
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Moving " + sourceFullPath + " to " + targetFullPath);
        }
        File sourceFile = new File(sourceFullPath);
        File targetFile = new File(targetFullPath);
        if (sourceFile.exists()) {
            File targetFolder = new File(targetFullPath.substring(0, targetFullPath.lastIndexOf("/")));
            if (!targetFolder.exists()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Creating " + targetFolder.getAbsolutePath());
                }
                if (targetFolder.mkdirs()) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Created " + targetFolder.getAbsolutePath());
                    }
                }
            }
            if (sourceFile.renameTo(targetFile)) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Success: moving " + sourceFullPath + " to " + targetFullPath);
                }
            } else {
                LOGGER.error("Failed: moving " + sourceFullPath + " to " + targetFullPath);
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(sourceFullPath + " does not exist.");
            }
        }

    }

    /**
     * <p>get new image path</p>
     *
     * @param imageName a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    private String getImagePath(String imageName) {
        String imagePath = getImageRoot()
                + "/" + currentNumTopFolders + "/" + currentNumFolders + "/" + currentNumImages
                + "/" + imageName;
        if (currentNumImages > numImagesInFolder) {
            currentNumImages = 1;
            currentNumFolders++;
        }
        if (currentNumFolders > numFoldersInFolder) {
            currentNumFolders = 1;
            currentNumTopFolders++;
        }
        return imagePath;
    }

    @Override
    protected Pattern getPattern() {
        return pattern;
    }

    /**
     * <p>set <field>pattern</field></p>
     *
     * @param pattern a {@link java.lang.String} object
     */
    public void setPattern(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public int getTargetGroup() {
        return targetGroup;
    }

    /**
     * <p>get <field>targetGroup</field></p>
     *
     * @param targetGroup an int
     */
    public void setTargetGroup(int targetGroup) {
        this.targetGroup = targetGroup;
    }

    /**
     * <p>get <field>imageRoot</field></p>
     *
     * @return  a {@link java.lang.String} object
     */
    public String getImageRoot() { return this.imageRoot; }

    /**
     * <p>set <field>imageRoot</field></p>
     *
     * @param imageRoot a {@link java.lang.String} object
     */
    public void setImageRoot(String imageRoot) { this.imageRoot = imageRoot; }

    /**
     * <p>get <field>originalImageRoot</field></p>
     *
     * @return  a {@link java.lang.String} object
     */
    public String getOriginalImageRoot() { return this.originalImageRoot; }

    /**
     * <p>set <field>originalImageRoot</field></p>
     *
     * @param originalImageRoot a {@link java.lang.String} object
     */
    public void setOriginalImageRoot(String originalImageRoot) { this.originalImageRoot = originalImageRoot; }

    /**
     * <p>get <field>targetImageFolder</field></p>
     *
     * @return  a {@link java.lang.String} object
     */
    public String getTargetImageFolder() { return this.targetImageFolder; }

    /**
     * <p>set <field>targetImageFolder</field></p>
     *
     * @param targetImageFolder a {@link java.lang.String} object
     */
    public void setTargetImageFolder(String targetImageFolder) { this.targetImageFolder = targetImageFolder; }

    /**
     * <p>get <field>sourceImageFolder</field></p>
     *
     * @return  a {@link java.lang.String} object
     */
    public String getSourceImageFolder() { return this.sourceImageFolder; }

    /**
     * <p>set <field>sourceImageFolder</field></p>
     *
     * @param sourceImageFolder a {@link java.lang.String} object
     */
    public void setSourceImageFolder(String sourceImageFolder) { this.sourceImageFolder = sourceImageFolder; }

    /**
     * <p>get <field>replacePattern</field></p>
     *
     * @return  a {@link java.lang.String} object
     */
    public String getReplacePattern() { return this.replacePattern; }

    /**
     * <p>set <field>replacePattern</field></p>
     *
     * @param replacePattern a {@link java.lang.String} object
     */
    public void setReplacePattern(String replacePattern) { this.replacePattern = replacePattern; }

    /**
     * <p>get <field>numFoldersInFolder</field></p>
     *
     * @return  an int
     */
    public int getNumFoldersInFolder() { return this.numFoldersInFolder; }

    /**
     * <p>set <field>numFoldersInFolder</field></p>
     *
     * @param numFoldersInFolder an int
     */
    public void setNumFoldersInFolder(int numFoldersInFolder) { this.numFoldersInFolder = numFoldersInFolder; }

    /**
     * <p>get <field>numImagesInFolder</field></p>
     *
     * @return  an int
     */
    public int getNumImagesInFolder() { return this.numImagesInFolder; }

    /**
     * <p>set <field>numImagesInFolder</field></p>
     *
     * @param numImagesInFolder an int
     */
    public void setNumImagesInFolder(int numImagesInFolder) { this.numImagesInFolder = numImagesInFolder; }

}
