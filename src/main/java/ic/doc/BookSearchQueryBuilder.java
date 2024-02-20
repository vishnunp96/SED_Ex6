package ic.doc;

public class BookSearchQueryBuilder {

    private String firstName;
    private String lastName;
    private String titleContains;
    private Integer publishedAfter;
    private Integer publishedBefore;


    public BookSearchQueryBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public BookSearchQueryBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BookSearchQueryBuilder withTitleContaining(String title) {
        this.titleContains = title;
        return this;
    }

    public BookSearchQueryBuilder wasPublishedAfter(Integer publishedAfter) {
        this.publishedAfter = publishedAfter;
        return this;
    }

    public BookSearchQueryBuilder wasPublishedBefore(Integer publishedBefore) {
        this.publishedBefore = publishedBefore;
        return this;
    }

    public BookSearchQuery build() {
        return new BookSearchQuery(this.firstName,
                this.lastName,
                this.titleContains,
                this.publishedAfter,
                this.publishedBefore
        );
    }
}
