import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';
import { EmbedVideoService } from 'ngx-embed-video';

import { IAnalysisSessionResource } from 'app/shared/model/analysis-session-resource.model';

@Component({
    selector: 'jhi-analysis-session-resource-detail',
    templateUrl: './analysis-session-resource-detail.component.html'
})
export class AnalysisSessionResourceDetailComponent implements OnInit {
    analysisSessionResource: IAnalysisSessionResource;

    constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute, protected embedService: EmbedVideoService) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ analysisSessionResource }) => {
            this.analysisSessionResource = analysisSessionResource;
        });
    }

    embedVideo(url) {
        return this.embedService.embed(url);
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }
}
