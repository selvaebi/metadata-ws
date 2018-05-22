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
import uk.ac.ebi.ampt2d.metadata.persistence.AccessionValidation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Sample {

    @ApiModelProperty(position = 1, required = true)
    @NotNull
    @Size(min = 1, max = 255)
    @JsonProperty
    @Id
    private String accession;

    @ApiModelProperty(position = 2, required = true)
    @Min(1)
    @JsonProperty
    private int version;

    @ApiModelProperty(position = 3, required = true)
    @Size(min = 1, max = 255)
    @NotNull
    @Column(nullable = false)
    @JsonProperty
    private String name;

    Sample() {
    }

    public Sample(String accession, int version, String name) {
        this.accession = accession;
        this.version = version;
        this.name = name;
    }

    @AssertTrue(message = "Please provide valid accession of pattern accession.version")
    private boolean isValidAccession() {
        return AccessionValidation.isValidAccession(this.accession, this.version);
    }

}
