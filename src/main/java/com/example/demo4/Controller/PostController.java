package com.example.demo4.Controller;
import com.example.demo4.Model.Post;
import com.example.demo4.Model.RequestPost;
import com.example.demo4.Model.Response;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/authors")
public class PostController {
    private Post readPostById(int id) {
        return posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElse(null);
    }
    List<Post> posts = new ArrayList<>();
    int nextId = 0;

    @PostMapping
    @Operation(summary = "Create new post")
    public ResponseEntity<Response<List<Post>>> addPost(@RequestBody RequestPost requestPost) {
        Post newPost = new Post();
        newPost.setId(nextId++);
        newPost.setTitle(requestPost.getTitle());
        newPost.setContent(requestPost.getContent());
        newPost.setAuthor(requestPost.getAuthor());
        newPost.setCreationDate(requestPost.getCreationDate());
        newPost.setTags(requestPost.getTags());
        posts.add(newPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new Response<>("Create new post successfully.", posts, HttpStatus.OK.value(), LocalDateTime.now())
        );
    }

    @GetMapping
    @Operation(summary = "Read all posts")
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(posts);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Read post by id")
    public ResponseEntity<Response> getPersonById(@PathVariable int id) {
        Post existingPost = readPostById(id);
        if (existingPost != null) {
            Response response = new Response("Read post successfully", existingPost, HttpStatus.OK.value(), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Update post by id")
    public ResponseEntity<Response> updatePersonById(@PathVariable int id, @RequestBody RequestPost requestPost) {
        Post existingPost = posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElse(null);
        if (existingPost != null) {
            existingPost.setTitle(requestPost.getTitle());
            existingPost.setContent(requestPost.getContent());
            existingPost.setAuthor(requestPost.getAuthor());
            existingPost.setCreationDate(requestPost.getCreationDate());
            existingPost.setTags(requestPost.getTags());

            Response response = new Response("Update post successfully", existingPost, HttpStatus.OK.value(), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Delete post by id")
    public ResponseEntity<Response> deletePostById(@PathVariable int id) {
        Post existingPost = posts.stream()
                .filter(post -> post.getId() == id)
                .findFirst()
                .orElse(null);
        if (existingPost != null) {
            posts.remove(existingPost);
            Response response = new Response("Deleted post successfully", existingPost, HttpStatus.OK.value(), LocalDateTime.now());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tags")
    @Operation(summary = "Get post by tags")
    public ResponseEntity<Response<List<Post>>> getPostByTags(@RequestParam List<String> tags) {
        List<Post> matchedPosts = posts.stream()
                .filter(post -> post.getTags().stream().anyMatch(tags::contains))
                .collect(Collectors.toList());
        if (matchedPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response<>("No posts found with the given tags", null, HttpStatus.NOT_FOUND.value(), LocalDateTime.now())
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
                new Response<>("Posts with matching tags found", matchedPosts, HttpStatus.OK.value(), LocalDateTime.now())
        );
    }

    @GetMapping("/author")
    @Operation(summary = "Get post by Author")
    public ResponseEntity<Response<Post>> getPostByAuthor(@RequestParam String author) {
        return posts.stream()
                .filter(post -> post.getAuthor().equals(author))
                .findFirst()
                .map(post -> ResponseEntity.status(HttpStatus.CREATED).body(
                        new Response<>("Post's Author found",post,HttpStatus.OK.value(),LocalDateTime.now())
                ))
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new Response<>("Post's Author not found",null,HttpStatus.NOT_FOUND.value(),LocalDateTime.now())
                ));
    }

    @GetMapping("/title")
    @Operation(summary = "Get post by Title")
    public ResponseEntity<Response<Post>> getPostByTitle(@RequestParam String title)
    {
        return posts.stream()
                .filter(post -> post.getTitle().equals(title))
                .findFirst()
                .map(post -> ResponseEntity.status(HttpStatus.CREATED).body(
                        new Response<>("Post's title found",post,HttpStatus.OK.value(),LocalDateTime.now())
                ))
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new Response<>("Post's title not found",null,HttpStatus.NOT_FOUND.value(),LocalDateTime.now())
                ));
    }

        @GetMapping("/paginated")
    @Operation(summary = "Pagination")
    public ResponseEntity<Response<List<Post>>> getPaginatedPosts(@RequestParam(defaultValue = "1") int page) {
        int pageSize = 3;
        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, posts.size());

        if (fromIndex >= posts.size()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new Response<>("The page has no post.", null, HttpStatus.NOT_FOUND.value(), LocalDateTime.now())
            );
        }

        List<Post> paginatedPosts = posts.subList(fromIndex, toIndex);

        return ResponseEntity.ok(
                new Response<>("Paginated posts successfully", paginatedPosts, HttpStatus.OK.value(), LocalDateTime.now())
        );
    }
}


