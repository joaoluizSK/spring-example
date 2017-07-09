package br.com.twodo.controller;

import br.com.twodo.model.StatusTitulo;
import br.com.twodo.repository.filter.TituloFilter;
import br.com.twodo.service.TituloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import br.com.twodo.model.Titulo;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/titulos")
public class TituloController {

    private static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
    private TituloService tituloService;

	@RequestMapping("/novo")
	public ModelAndView novo(){
	    ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
	    mv.addObject(new Titulo());
	    mv.addObject("todosStatusTitulo", StatusTitulo.values());
		return mv;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes){

        if(errors.hasErrors()){
            return CADASTRO_VIEW;
        }

        try{

            tituloService.salvar(titulo);

            attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
            return "redirect:/titulos/novo";
        } catch (IllegalArgumentException e){
            errors.rejectValue("dataVencimento",null, e.getMessage());
            return CADASTRO_VIEW;
        }
	}

	@RequestMapping
    public ModelAndView pesquisar(@ModelAttribute("filtro") TituloFilter filtro){
        ModelAndView mav = new ModelAndView("PesquisaTitulos");

        String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();

	    List<Titulo> todos = tituloService.listarTituloPorDescricao(descricao);
	    mav.addObject("titulos",todos);

	    return mav;
    }

    @RequestMapping("{codigo}")
    public ModelAndView edicao(@PathVariable("codigo") Titulo titulo){
        ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
        mv.addObject("titulo",titulo);
        return mv;
    }

    @RequestMapping(value = "/{codigo}/receber", method = RequestMethod.PUT)
    public @ResponseBody String receber(@PathVariable Long codigo){
        return tituloService.receber(codigo);
    }

    @RequestMapping(value="{codigo}",method = RequestMethod.DELETE)
    public String excluir(@PathVariable Long codigo, RedirectAttributes attributes){
        tituloService.excluir(codigo);
        attributes.addFlashAttribute("mensagem", "Título removido com sucesso!");
		return "redirect:/titulos";
	}

	@ModelAttribute(name = "todosStatusTitulo")
    public List<StatusTitulo> todosStatusTitulo(){
	    return Arrays.asList(StatusTitulo.values());
    }
	
}
