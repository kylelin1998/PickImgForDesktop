package code.ui;

import code.config.I18nEnum;
import code.config.SchemeConfig;
import code.config.SchemeItemEnum;
import code.ui.annotation.FieldAnnotation;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SchemeWindow {

    protected static JFrame frame;
    protected static SchemeItemEnum schemeItemEnum;

    protected static Map<String, JTextField> fieldInputList = new LinkedHashMap<>();

    public static JFrame render(SchemeItemEnum s) {
        schemeItemEnum = s;
        frame = new JFrame(schemeItemEnum.getName() + " - " + I18nEnum.Title.getText());

        ImageIcon img = new ImageIcon(SchemeWindow.class.getResource("icon.png"));
        frame.setIconImage(img.getImage());
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        renderAll();

        frame.pack();
        frame.setVisible(true);

        return frame;
    }

    private static void renderAll() {
        Box vBox = Box.createVerticalBox();

        JButton saveButton = new JButton(I18nEnum.SaveButton.getText());

        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(saveButton);

        vBox.add(Box.createVerticalStrut(20));
        vBox.add(horizontalBox);

        Box vBox2 = Box.createVerticalBox();
        renderForm(vBox2, saveButton);

        vBox.add(vBox2);
        vBox.add(Box.createVerticalStrut(30));

        frame.setContentPane(vBox);
    }

    private static void renderForm(JComponent panel, JButton saveButton) {
        try {
            SchemeConfig.SchemeEntity schemeEntity = SchemeConfig.readConfig();

            SchemeConfig.SchemeItemEntity schemeItemEntity = getSchemeItemEntity(schemeEntity.getItemList(), schemeEntity.getCurrentScheme());
            switch (schemeItemEnum) {
                case Github:
                    SchemeConfig.SchemeItemGithubSettingEntity github = schemeItemEntity.getGithub();
                    if (null == github) {
                        github = new SchemeConfig.SchemeItemGithubSettingEntity();
                        schemeItemEntity.setGithub(github);
                    }
                    renderFormByReflect(github, schemeEntity, panel, saveButton);
                    break;
                case Imgur:
                    SchemeConfig.SchemeItemImgurSettingEntity imgur = schemeItemEntity.getImgur();
                    if (null == imgur) {
                        imgur = new SchemeConfig.SchemeItemImgurSettingEntity();
                        schemeItemEntity.setImgur(imgur);
                    }
                    renderFormByReflect(imgur, schemeEntity, panel, saveButton);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void renderFormByReflect(Object o, SchemeConfig.SchemeEntity schemeEntity, JComponent panel, JButton saveButton) throws IllegalAccessException {
        for (Field field : o.getClass().getDeclaredFields()) {
            FieldAnnotation annotation = field.getAnnotation(FieldAnnotation.class);
            if (null != annotation) {
                JTextField formTextField = createFormTextField(panel, annotation.required(),annotation.displayText() + ": ", (String) field.get(o));
                fieldInputList.put(field.getName(), formTextField);
            }
        }

        saveButton.addActionListener((actionEvent) -> {
            for (Field field : o.getClass().getDeclaredFields()) {
                FieldAnnotation annotation = field.getAnnotation(FieldAnnotation.class);
                if (null != annotation) {
                    JTextField jTextField = fieldInputList.get(field.getName());
                    String text = jTextField.getText();
                    if (annotation.required()) {
                        if (StringUtils.isBlank(text)) {
                            MessageUI.warning(frame, I18nEnum.EmptyInputPrompt.getText(annotation.displayText()));
                            return;
                        }
                    }

                    try {
                        field.set(o, text);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            SchemeConfig.saveConfig(schemeEntity);

            MessageUI.info(frame, I18nEnum.SaveSuccess.getText());
        });
    }

    private static SchemeConfig.SchemeItemEntity getSchemeItemEntity(List<SchemeConfig.SchemeItemEntity> itemList, String name) {
        for (SchemeConfig.SchemeItemEntity schemeItemEntity : itemList) {
            if (schemeItemEntity.getName().equals(name)) {
                return schemeItemEntity;
            }
        }
        return null;
    }

    private static JTextField createFormTextField(JComponent panel, boolean required, String label, String text) {
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(Box.createHorizontalStrut(30));

        if (required) {
            JLabel jLabel = new JLabel("*");
            jLabel.setForeground(Color.RED);
            horizontalBox.add(jLabel);
            horizontalBox.add(Box.createHorizontalStrut(2));
        }

        JLabel jLabel = new JLabel(label);

        horizontalBox.add(jLabel);
        JTextField jText = new JTextField(20);
        jText.setText(text);
        horizontalBox.add(jText);
        horizontalBox.add(Box.createHorizontalStrut(30));

        panel.add(Box.createVerticalStrut(15));
        panel.add(horizontalBox);

        return jText;
    }

}
