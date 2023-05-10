package me.dragosghinea.application.config;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractCsvLayout;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;
import org.apache.logging.log4j.status.StatusLogger;

@Plugin(name = "CustomCsvEventLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class CustomCsvEventLayout extends AbstractCsvLayout {

    @PluginFactory
    public static CustomCsvEventLayout createLayout(
            @PluginConfiguration final Configuration config,
            @PluginAttribute(value = "format", defaultString = DEFAULT_FORMAT) final String format,
            @PluginAttribute("delimiter") final Character delimiter,
            @PluginAttribute("escape") final Character escape, @PluginAttribute("quote") final Character quote,
            @PluginAttribute("quoteMode") final QuoteMode quoteMode,
            @PluginAttribute("nullString") final String nullString,
            @PluginAttribute("recordSeparator") final String recordSeparator,
            @PluginAttribute(value = "charset", defaultString = DEFAULT_CHARSET) final Charset charset,
            @PluginAttribute("header") final String header, @PluginAttribute("footer") final String footer)
    {

        final CSVFormat csvFormat = createFormat(format, delimiter, escape, quote, quoteMode, nullString,
                recordSeparator);
        return new CustomCsvEventLayout(config, charset, csvFormat, header, footer);
    }

    protected CustomCsvEventLayout(final Configuration config, final Charset charset, final CSVFormat csvFormat,
                                   final String header, final String footer) {
        super(config, charset, csvFormat, header, footer);
    }

    private final static FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String toSerializable(final LogEvent event) {
        final StringBuilder buffer = getStringBuilder();
        final CSVFormat format = getFormat();
        try {
            format.print(event.getMessage().getFormat(), buffer, true);
            for(Object parameter : event.getMessage().getParameters()){
                format.print(parameter, buffer, false);
            }
            format.print(event.getLevel(), buffer, false);
            format.print(dateFormat.format(event.getTimeMillis()), buffer, false);
            format.println(buffer);

            return buffer.toString();
        } catch (final IOException e) {
            StatusLogger.getLogger().error(event.toString(), e);
            return format.getCommentMarker() + " " + e;
        }
    }

}