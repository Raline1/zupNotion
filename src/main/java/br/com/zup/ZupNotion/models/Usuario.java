package br.com.zup.ZupNotion.models;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String nome;
    @Column(nullable = false)
    private String senha;
    @OneToMany
    private List<Tarefa> tarefas;

}
