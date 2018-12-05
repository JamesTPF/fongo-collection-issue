package persistance.nosql;

import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.github.fakemongo.Fongo;
import org.joda.time.LocalDate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.marshall.jackson.JacksonMapper;
import org.jongo.marshall.jackson.oid.MongoId;
import org.junit.Before;
import org.junit.Test;

public class ExampleTest {
    private Fongo fongo = new Fongo("someDatabase");
    private Jongo jongo;


    @Before
    public void createJongo() {
        jongo = new Jongo(fongo.getDB("exampleDatabase"), new JacksonMapper.Builder()
                .registerModule(new JodaModule())
                .build());
    }

    @Test
    public void storeAndRetrieveObject() {
        final MongoCollection myCollection = jongo.getCollection("myCollection");
        final TestDocument document = new TestDocument("anyId");
        myCollection.update("{'_id': #}", document.getId()).upsert().with(document);
        myCollection.findOne("{'_id': #}", document.getId()).as(TestDocument.class);
    }

    private static class TestDocument {

        @MongoId
        private String id;
        private LocalDate testLocalDate;

        TestDocument() {
        }

        public TestDocument(final String id) {
            this.id = id;
            this.testLocalDate = new LocalDate();
        }

        public String getId() {
            return id;
        }

        public void setId(final String id) {
            this.id = id;
        }

        public LocalDate getTestLocalDate() {
            return testLocalDate;
        }

        public void setTestLocalDate(LocalDate testLocalDate) {
            this.testLocalDate = testLocalDate;
        }
    }
}
