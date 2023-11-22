package com.korea.test;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/notebook")
public class NotebookController {

    @Autowired
    private NotebookRepository notebookRepository;

    @Autowired
    private PostRepository postRepository;

    @Controller
    @RequestMapping("/notebook")
    public class NotebookContriller {

        @Autowired
        private NotebookRepository notebookRepository;

        @Autowired
        private PostRepository postRepository;

        // 노트북 목록 조회
        @GetMapping("/list")
        public String listNotebooks(Model model) {
            List<Notebook> notebookList = notebookRepository.findAll();
            model.addAttribute("notebookList", notebookList);
            return "notebook_list_template";
        }

        // 노트북 상세 정보 조회
        @GetMapping("/{id}")
        public String getNotebook(@PathVariable Long id, Model model) {
            Notebook targetNotebook = notebookRepository.findById(id).orElse(null);

            if (targetNotebook != null) {
                List<Post> posts = postRepository.findByNotebook(targetNotebook);
                model.addAttribute("targetNotebook", targetNotebook);
                model.addAttribute("postList", posts);
            }
            return "your_notebook_detail_template";
        }


        @GetMapping("/add")
        public String showAddNotebookForm(Model model) {
            model.addAttribute("notebook", new Notebook());
            return "add_notebook_template";
        }

        // 노트북 추가 처리
        @PostMapping("/add")
        public String addNotebook(@ModelAttribute Notebook notebook) {
            if (notebook.getName() == null || notebook.getName().trim().isEmpty()) {
                notebook.setName("새노트");
            }
            notebookRepository.save(notebook);
            return "redirect:/notebook/list";
        }
    }
}