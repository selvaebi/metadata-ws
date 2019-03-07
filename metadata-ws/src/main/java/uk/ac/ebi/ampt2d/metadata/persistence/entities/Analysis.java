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
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"accession", "version"}))
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "ANALYSIS_SEQ", sequenceName = "analysis_sequence")
public class Analysis extends Auditable<Long> {

    public enum Type {

        CASE_CONTROL,

        CONTROL_SET,

        CASE_SET,

        COLLECTION,

        TUMOR,

        MATCHED_NORMAL

    }

    public enum Technology {

        GWAS,

        EXOME_SEQUENCING,

        GENOTYPING,

        ARRAY,

        CURATION

    }

    @ApiModelProperty(position = 1, value = "Analysis auto generated id", required = true, readOnly = true)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANALYSIS_SEQ")
    private long id;

    @ApiModelProperty(position = 2)
    @Embedded
    @Valid
    private AccessionVersionId accessionVersionId;

    @ApiModelProperty(position = 3, required = true)
    @Size(min = 1, max = 255)
    @NotNull
    @JsonProperty
    @Column(nullable = false)
    private String name;

    @ApiModelProperty(position = 4, required = true)
    @NotNull
    @NotBlank
    @JsonProperty
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @ApiModelProperty(position = 5, dataType = "java.lang.String", notes = "Url to a Study")
    @JsonProperty
    @ManyToOne(optional = false)
    private Study study;

    @ApiModelProperty(position = 6, dataType = "java.lang.String", example = "[url1, url2]",
            notes = "URL(s) to the reference sequence(s): either a single URL to an assembly/transcriptome, or a comma-separated list of gene sequences")
    @JsonProperty
    @ManyToMany
    private List<ReferenceSequence> referenceSequences;

    @ApiModelProperty(position = 7, required = true)
    @NotNull
    @JsonProperty
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Technology technology;

    @ApiModelProperty(position = 8, required = true)
    @JsonProperty
    @Enumerated(EnumType.STRING)
    private Type type;

    @ApiModelProperty(position = 9, required = true)
    @Size(min = 1, max = 255)
    @JsonProperty
    private String platform;

    @ManyToMany
    private List<Sample> samples;

    @ManyToMany
    private List<File> files;

    public Analysis() {
    }

    public Analysis(AccessionVersionId accessionVersionId, String name, String description,
                    Study study, List<ReferenceSequence> referenceSequences, Technology technology,
                    List<File> files,List<Sample> samples) {
        this.accessionVersionId = accessionVersionId;
        this.name = name;
        this.description = description;
        this.study = study;
        this.referenceSequences = referenceSequences;
        this.technology = technology;
        this.files = files;
        this.samples=samples;
    }

    @Override
    public Long getId() {
        return id;
    }

    public AccessionVersionId getAccessionVersionId() {
        return accessionVersionId;
    }

    public Study getStudy() {
        return study;
    }

    public Technology getTechnology() {
        return technology;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<ReferenceSequence> getReferenceSequences() {
        return referenceSequences;
    }

}