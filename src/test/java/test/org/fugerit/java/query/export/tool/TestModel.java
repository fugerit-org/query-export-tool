package test.org.fugerit.java.query.export.tool;

import org.fugerit.java.query.export.meta.MetaField;
import org.junit.Assert;
import org.junit.Test;

public class TestModel {

    @Test
    public void testMetaField() {
        MetaField field = new MetaField() {
            @Override
            public String getStringValue() {
                return "";
            }
        };
        Assert.assertNull(  field.getNumberValue() );
        Assert.assertNull(  field.getTimestampValue() );
        Assert.assertEquals( "", field.getStringValue() );
        Assert.assertEquals( MetaField.TYPE_STRING, field.getType() );
    }

}
