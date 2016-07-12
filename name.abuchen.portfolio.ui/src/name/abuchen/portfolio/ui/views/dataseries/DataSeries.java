package name.abuchen.portfolio.ui.views.dataseries;

import java.util.Locale;
import java.util.function.Function;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.swtchart.LineStyle;

import name.abuchen.portfolio.model.Account;
import name.abuchen.portfolio.model.Classification;
import name.abuchen.portfolio.model.Portfolio;
import name.abuchen.portfolio.model.Security;
import name.abuchen.portfolio.ui.Images;
import name.abuchen.portfolio.ui.Messages;

/**
 * A data series available to add to charts.
 */
public final class DataSeries
{
    /**
     * The use case determines the selection of data series available.
     */
    public enum UseCase
    {
        STATEMENT_OF_ASSETS, PERFORMANCE, RETURN_VOLATILITY
    }

    /**
     * Data series available for the Client type.
     */
    public enum ClientDataSeries
    {
        TOTALS, INVESTED_CAPITAL, TRANSFERALS, TAXES, ABSOLUTE_DELTA, DIVIDENDS, DIVIDENDS_ACCUMULATED, INTEREST, INTEREST_ACCUMULATED, DELTA_PERCENTAGE;
    }

    /**
     * Type of objects for which the PerformanceIndex is calculated.
     */
    public enum Type
    {
        CLIENT("Client-", i -> ((ClientDataSeries) i).name().toLowerCase(Locale.US)), //$NON-NLS-1$
        SECURITY("Security", i -> ((Security) i).getUUID()), //$NON-NLS-1$
        SECURITY_BENCHMARK("[b]Security", i -> ((Security) i).getUUID()), //$NON-NLS-1$
        ACCOUNT("Account", i -> ((Account) i).getUUID()), //$NON-NLS-1$
        PORTFOLIO("Portfolio", i -> ((Portfolio) i).getUUID()), //$NON-NLS-1$
        PORTFOLIO_PLUS_ACCOUNT("[+]Portfolio", i -> ((Portfolio) i).getUUID()), //$NON-NLS-1$
        CONSUMER_PRICE_INDEX("[b]ConsumerPriceIndex", i -> ""), //$NON-NLS-1$ //$NON-NLS-2$
        CLASSIFICATION("Classification", i -> ((Classification) i).getId()); //$NON-NLS-1$

        private final String label;
        private final Function<Object, String> uuidProvider;

        Type(String label, Function<Object, String> uuidProvider)
        {
            this.label = label;
            this.uuidProvider = uuidProvider;
        }

        String buildUUID(Object instance)
        {
            return label + uuidProvider.apply(instance);
        }
    }

    private Type type;
    private Object instance;
    private String label;
    private boolean isLineChart = true;
    private boolean isBenchmark = false;

    private RGB color;

    private boolean showArea;
    private LineStyle lineStyle = LineStyle.SOLID;

    /* package */ DataSeries(Type type, Object instance, String label, RGB color)
    {
        this.type = type;
        this.instance = instance;
        this.label = label;
        this.color = color;
    }

    public Type getType()
    {
        return type;
    }

    public Object getInstance()
    {
        return instance;
    }

    public String getLabel()
    {
        return isBenchmark() ? label + Messages.ChartSeriesBenchmarkSuffix : label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public String getSearchLabel()
    {
        StringBuilder buf = new StringBuilder();

        buf.append(label);

        if (instance instanceof Classification)
        {
            Classification parent = ((Classification) instance).getParent();
            buf.append(" (").append(parent.getPathName(true)).append(")"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (isBenchmark())
            buf.append(Messages.ChartSeriesBenchmarkSuffix);

        return buf.toString();
    }

    public void setColor(RGB color)
    {
        this.color = color;
    }

    public RGB getColor()
    {
        return color;
    }

    public boolean isLineChart()
    {
        return isLineChart;
    }

    public void setLineChart(boolean isLineChart)
    {
        this.isLineChart = isLineChart;
    }

    public boolean isBenchmark()
    {
        return isBenchmark;
    }

    public void setBenchmark(boolean isBenchmark)
    {
        this.isBenchmark = isBenchmark;
    }

    public boolean isShowArea()
    {
        return showArea;
    }

    public void setShowArea(boolean showArea)
    {
        this.showArea = showArea;
    }

    public LineStyle getLineStyle()
    {
        return lineStyle;
    }

    public void setLineStyle(LineStyle lineStyle)
    {
        this.lineStyle = lineStyle;
    }

    public Image getImage()
    {
        switch (type)
        {
            case SECURITY:
            case SECURITY_BENCHMARK:
                return Images.SECURITY.image();
            case ACCOUNT:
                return Images.ACCOUNT.image();
            case PORTFOLIO:
            case PORTFOLIO_PLUS_ACCOUNT:
                return Images.PORTFOLIO.image();
            case CLASSIFICATION:
                return Images.CATEGORY.image();
            default:
                return null;
        }
    }

    public String getUUID()
    {
        return this.type.buildUUID(instance);
    }

    @Override
    public String toString()
    {
        return getSearchLabel() + " [" + getUUID() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }
}
