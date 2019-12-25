package file.tofile.converter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class Convert {

    public static final Logger logger = Logger.getLogger(Convert.class);

    public static void main(String[] args) {
        if (args.length <= 1) {
            logger.error("Please Enter two arguments while executing.");
            logger.info("Argument 1: Source Directory");
            logger.info("Argument 2: Source File");
            logger.info("Argument 3: Destination Directory");
            return;
        }
        pdfToImage(args);
    }

    public static void pdfToImage(String[] args) {
        try {
            String sourceDir = args[0];
            String destinationDir = args[0].replaceAll(File.separator + "$", File.separator + args[2]) + File.separator;
            String sourceFileStr = sourceDir + File.separator + args[1];
            File sourceFile = new File(sourceFileStr);
            File destinationFile = new File(destinationDir);
            if (!destinationFile.exists()) {
                destinationFile.mkdir();
                logger.info("Folder Created -> " + destinationFile.getAbsolutePath());
            }
            if (sourceFile.exists()) {
                PDDocument document = PDDocument.load(sourceFileStr);
                List<PDPage> list = document.getDocumentCatalog().getAllPages();
                String fileName = sourceFile.getName().replace(".pdf", "");
                int pageNumber = 1;
                for (PDPage page : list) {
                    BufferedImage image = page.convertToImage();
                    File outputFile = new File(destinationDir + fileName + "_" + pageNumber + ".png");
                    ImageIO.write(image, "png", outputFile);
                    pageNumber++;
                }
                document.close();
                logger.debug("Image saved at -> " + destinationFile.getAbsolutePath());
            }
            else {
                logger.error(sourceFile.getName() + " File does not exist");
            }
        }
        catch (Exception e) {
            logger.error("Exception occurred while converting", e);
        }
        finally {
            logger.info("Completed Processing");
        }
    }
}
