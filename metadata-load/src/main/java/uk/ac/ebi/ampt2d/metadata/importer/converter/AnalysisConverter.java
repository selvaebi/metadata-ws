/*
 *
 * Copyright 2019 EMBL - European Bioinformatics Institute
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

package uk.ac.ebi.ampt2d.metadata.importer.converter;

import org.springframework.core.convert.converter.Converter;
import uk.ac.ebi.ampt2d.metadata.importer.extractor.FileExtractorFromAnalysis;
import uk.ac.ebi.ampt2d.metadata.importer.extractor.ReferenceSequenceExtractorFromAnalysis;
import uk.ac.ebi.ampt2d.metadata.importer.extractor.SampleExtractor;
import uk.ac.ebi.ampt2d.metadata.importer.extractor.StudyExtractor;
import uk.ac.ebi.ampt2d.metadata.persistence.entities.AccessionVersionId;
import uk.ac.ebi.ampt2d.metadata.persistence.entities.Analysis;
import uk.ac.ebi.ampt2d.metadata.persistence.entities.File;
import uk.ac.ebi.ampt2d.metadata.persistence.entities.ReferenceSequence;
import uk.ac.ebi.ampt2d.metadata.persistence.entities.Sample;
import uk.ac.ebi.ampt2d.metadata.persistence.entities.Study;
import uk.ac.ebi.ena.sra.xml.AnalysisType;
import uk.ac.ebi.ena.sra.xml.AnalysisType.ANALYSISTYPE.SEQUENCEVARIATION;

import java.util.Arrays;
import java.util.List;

public class AnalysisConverter implements Converter<AnalysisType, Analysis> {

    private ReferenceSequenceExtractorFromAnalysis referenceSequenceExtractorFromAnalysis;

    private FileExtractorFromAnalysis fileExtractorFromAnalysis;

    private StudyExtractor studyExtractor;

    private SampleExtractor sampleExtractor;

    public AnalysisConverter(StudyExtractor studyExtractor,
                             ReferenceSequenceExtractorFromAnalysis referenceSequenceExtractorFromAnalysis,
                             FileExtractorFromAnalysis fileExtractorFromAnalysis,
                             SampleExtractor sampleExtractor) {
        this.studyExtractor = studyExtractor;
        this.referenceSequenceExtractorFromAnalysis = referenceSequenceExtractorFromAnalysis;
        this.fileExtractorFromAnalysis = fileExtractorFromAnalysis;
        this.sampleExtractor = sampleExtractor;
    }

    @Override
    public Analysis convert(AnalysisType analysisType) {
        return new Analysis(new AccessionVersionId(analysisType.getAccession(), 1), analysisType.getTITLE(),
                analysisType.getDESCRIPTION(), getStudy(), getReferenceType(analysisType),
                getTechnology(analysisType), getPlatform(analysisType), getfiles(analysisType), getSamples());
    }

    private String getPlatform(AnalysisType analysisType) {
        SEQUENCEVARIATION sequencevariation = analysisType.getANALYSISTYPE().getSEQUENCEVARIATION();
        String platform = "UNSPECIFIED";
        if (sequencevariation != null && sequencevariation.getPLATFORM() != null) {
            platform = sequencevariation.getPLATFORM();
        }
        return platform;
    }

    private List<Sample> getSamples() {
        return Arrays.asList(sampleExtractor.getSample());
    }

    private Study getStudy() {
        return studyExtractor.getStudy();
    }

    private List<File> getfiles(AnalysisType analysisType) {
        return fileExtractorFromAnalysis.extractFilesFromAnalysis(analysisType);
    }

    private List<ReferenceSequence> getReferenceType(AnalysisType analysisType) {
        return referenceSequenceExtractorFromAnalysis.getReferenceType(analysisType);
    }

    private Analysis.Technology getTechnology(AnalysisType analysisType) {
        SEQUENCEVARIATION sequencevariation = analysisType.getANALYSISTYPE().getSEQUENCEVARIATION();
        if (sequencevariation != null && sequencevariation.getEXPERIMENTTYPEArray().length != 0) {
            switch (sequencevariation.getEXPERIMENTTYPEArray()[0].intValue()) {
                case 2:
                    return Analysis.Technology.EXOME_SEQUENCING;
                case 3:
                    return Analysis.Technology.GENOTYPING;
                case 5:
                    return Analysis.Technology.CURATION;
                case 6:
                    return Analysis.Technology.GENOTYPING;
                default:
                    return Analysis.Technology.GWAS;
            }
        }
        return Analysis.Technology.UNSPECIFIED;
    }
}
