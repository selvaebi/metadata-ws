/*
 *
 * Copyright 2018 EMBL - European Bioinformatics Institute
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package uk.ac.ebi.ampt2d.metadata.persistence.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name","patch"}))
@SequenceGenerator(initialValue=1, allocationSize=1 , name="REFERENCE_SEQUENCE_SEQ",
        sequenceName="reference_sequence_sequence")
public class ReferenceSequence extends Auditable<Long> {

    public enum Type {

        ASSEMBLY,

        GENE,

        TRANSCRIPTOME

    }

    @ApiModelProperty(position = 1, value = "Reference Sequence auto generated id", required = true, readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="REFERENCE_SEQUENCE_SEQ")
    private long id;

    @ApiModelProperty(position = 2, required = true)
    @Size(min = 1, max = 255)
    @NotNull
    @JsonProperty
    @Column(nullable = false)
    private String name;

    @ApiModelProperty(position = 3, required = true)
    @JsonProperty
    @Column(nullable = true)
    private String patch;

    @ApiModelProperty(position = 4, required = true)
    @NotNull
    @JsonProperty
    @ElementCollection
    private List<String> accessions = new ArrayList<String>();

    @ApiModelProperty(position = 5, required = true)
    @NotNull
    @JsonProperty
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    ReferenceSequence() {}

    public ReferenceSequence(String name, String patch, List<String> accessions, Type type) {
        this.name = name;
        this.patch = patch;
        this.accessions = accessions;
        this.type = type;
    }

    @Override
    public Long getId() {
        return id;
    }

    public ReferenceSequence(String name) {
        this.name = name;
    }

    public List<String> getAccessions() {
        return accessions;
    }

    public Type getType() {
        return type;
    }

}
