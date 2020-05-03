import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MaTestModule } from '../../../test.module';
import { DataSetResourceComponent } from 'app/entities/data-set-resource/data-set-resource.component';
import { DataSetResourceService } from 'app/entities/data-set-resource/data-set-resource.service';
import { DataSetResource } from 'app/shared/model/data-set-resource.model';

describe('Component Tests', () => {
    describe('DataSetResource Management Component', () => {
        let comp: DataSetResourceComponent;
        let fixture: ComponentFixture<DataSetResourceComponent>;
        let service: DataSetResourceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [DataSetResourceComponent],
                providers: []
            })
                .overrideTemplate(DataSetResourceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataSetResourceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataSetResourceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DataSetResource(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.dataSetResources[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
