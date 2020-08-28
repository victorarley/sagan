package sagan.site.blog;

import java.util.Date;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sagan.blog.Post;
import sagan.blog.PostBuilder;
import sagan.support.DateFactory;
import sagan.support.DateTestUtils;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BlogService_UpdatePostTests {

    private BlogService service;
    private Post post;
    private Date publishAt = DateTestUtils.getDate("2013-07-01 12:00");
    private Date now = DateTestUtils.getDate("2013-07-01 13:00");

    @Mock
    private PostRepository postRepository;

    @Mock
    private DateFactory dateFactory;

    @Mock
    private PostFormAdapter postFormAdapter;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    private PostForm postForm;

    @Before
    public void setup() {
        given(dateFactory.now()).willReturn(now);

        service = new BlogService(postRepository, postFormAdapter, dateFactory);

        post = PostBuilder.post().id(123L).publishAt(publishAt).build();

        postForm = new PostForm(post);
        service.updatePost(post, postForm);
    }

    @Test
    public void postIsUpdated() {
        verify(postFormAdapter).updatePostFromPostForm(post, postForm);
    }

    @Test
    public void postIsPersisted() {
        verify(postRepository).save(post);
    }

}
