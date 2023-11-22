package com.korea.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TestController {

    @Autowired
    private PostRepository postRepository;

    @RequestMapping("/test")
    @ResponseBody
    public String test() {
        return "main";
    }

    @RequestMapping("/")
    public String main(Model model) {
        List<Post> postList = postRepository.findAll();
        model.addAttribute("postList", postList);
        if (postList.isEmpty()) {
            Post defaultPost = new Post();
            defaultPost.setTitle("기본 제목");
            defaultPost.setContent("기본 내용");
            defaultPost.setCreateDate(LocalDateTime.now());
            postRepository.save(defaultPost);
            postList = postRepository.findAll();
        }
        model.addAttribute("targetPost", postList.get(0));
        return "main";
    }

    @PostMapping("/write")
    public String write() {
        Post post = new Post();
        post.setTitle("new title..");
        post.setContent("");
        post.setCreateDate(LocalDateTime.now());
        postRepository.save(post);
        return "redirect:/";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable Long id) {
        Post post = postRepository.findById(id).get();
        model.addAttribute("targetPost", post);
        model.addAttribute("postList", postRepository.findAll());
        return "main";
    }

    @PostMapping("/update")
    public String update(Long id, String title, String content, Model model) {
        Post post = postRepository.findById(id).orElseThrow();
        if (title == null || title.trim().isEmpty()) {
            post.setTitle("제목 없음");
        } else {
            post.setTitle(title);
        }
        post.setContent(content);

        postRepository.save(post);

        return "redirect:/detail/" + id;
    }

    @PostMapping("/delete")
    public String delete(Long id) {
        postRepository.deleteById(id);
        return "redirect:/";
    }
}
