package Interfaz.TextEditor;

import java.awt.Color;
import java.awt.Font;

import javax.swing.text.Element;
import javax.swing.JTextPane;

@SuppressWarnings("serial")
public class LineNumberingTextArea extends JTextPane
{
    private JTextPane textArea;

    public LineNumberingTextArea(JTextPane textArea, Font f,Color bg, Color fg)
    {
        this.textArea = textArea;
        setEditable(false);
        setFont(f);
		setBackground(bg);
		setForeground(fg);
    }

    public void updateLineNumbers()
    {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }

    private String getLineNumbersText()
    {
        int caretPosition = textArea.getDocument().getLength();
        Element root = textArea.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();
        lineNumbersTextBuilder.append("1").append(System.lineSeparator());

        for (int elementIndex = 2; elementIndex < root.getElementIndex(caretPosition) + 2; elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(System.lineSeparator());
        }

        return lineNumbersTextBuilder.toString();
    }
}